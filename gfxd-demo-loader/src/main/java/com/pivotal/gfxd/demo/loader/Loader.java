/*==========================================================================
 * Copyright (c) 2014 Pivotal Software Inc. All Rights Reserved.
 *
 * This product is licensed to you under the Apache License, Version 2.0 (the "License").
 * You may not use this product except in compliance with the License.
 *
 * This product may include a number of subcomponents with separate copyright
 * notices and license terms. Your use of these subcomponents is subject to
 * the terms and conditions of the subcomponent's license, as noted in the
 * LICENSE file.
 *==========================================================================
 */

package com.pivotal.gfxd.demo.loader;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.List;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Component;

@Component("loader")
public class Loader extends JdbcDaoSupport implements ILoader {

  private static final Logger LOG = LoggerFactory.getLogger(
      Loader.class.getName());

  final int MINUTES_PER_INTERVAL = 5; // 5 minute unit slots

  private long rowsInserted;

  private float disturbance;

  @Autowired
  public Loader(DataSource dataSource) {
    super.setDataSource(dataSource);
  }

  public void insertBatch(final List<String[]> lines, final long timestamp) {
    String sql = "insert into raw_sensor (id, timestamp, value, property, plug_id, household_id, house_id, weekday, time_slice) values (?,?,?,?,?,?,?,?,?)";
    final Calendar cal = Calendar.getInstance();

    getJdbcTemplate().batchUpdate(sql, new BatchPreparedStatementSetter() {
      @Override
      public void setValues(PreparedStatement ps, int i)
          throws SQLException {
        final String[] split = lines.get(i);
        int plugId = Integer.parseInt(split[4]);
        int householdId = Integer.parseInt(split[5]);
        int houseId = Integer.parseInt(split[6]);

        ps.setLong(1, Long.parseLong(split[0]));
        ps.setLong(2, timestamp);
        float value = Float.parseFloat(split[2]);
        ps.setFloat(3, value + value * disturbance);
        ps.setInt(4, Integer.parseInt(split[3]));
        ps.setInt(5, computePlugId(plugId, householdId, houseId));
        ps.setInt(6, householdId);
        ps.setInt(7, houseId);

        cal.setTimeInMillis(timestamp * 1000L);
        int weekDay = cal.get(Calendar.DAY_OF_WEEK);
        int timeSlice = (cal.get(Calendar.HOUR_OF_DAY) * 60 + cal.get(
            Calendar.MINUTE)) / MINUTES_PER_INTERVAL;

        ps.setInt(8, weekDay); // weekday
        ps.setInt(9, timeSlice); // time_slice
      }

      @Override
      public int getBatchSize() {
        return lines.size();
      }
    });

    cal.setTimeInMillis(timestamp * 1000L);
    int weekDay = cal.get(Calendar.DAY_OF_WEEK);
    int timeSlice = (cal.get(Calendar.HOUR_OF_DAY) * 60 + cal.get(
        Calendar.MINUTE)) / MINUTES_PER_INTERVAL;

    LOG.debug("rows=" + lines.size() +
        " weekday=" + weekDay + " slice=" + timeSlice +
        " stamp=" + timestamp + " now=" + (int)(System.currentTimeMillis() / 1000));

    rowsInserted += lines.size();
  }

  private int computePlugId(int plugId, int householdId, int houseId) {
    int result = 19;
    result = 91 * result + plugId * 7;
    result = 91 * result + householdId * 7;
    result = 91 * result + houseId * 7;
    return result;
  }

  public long getRowsInserted() {
    return rowsInserted;
  }

  public void setDisturbance(float disturbance) {
    this.disturbance = disturbance;
    LOG.debug("--->>> disturbance = " + disturbance);
  }
}
