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

package com.pivotal.gfxd.demo;

import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import static org.junit.Assert.assertEquals;

/**
 * @author Jens Deppe
 */
public class TimeSliceTest {

  private static final String DEFAULT_TZ = "PST";
  private Calendar cal;

  @Before
  public void setup() {
    cal = GregorianCalendar.getInstance(TimeZone.getTimeZone(DEFAULT_TZ));
    // This is Tuesday, 21/01/2014 11:06:42.978
    cal.setTimeInMillis(1390331202978L);
  }

  @Test
  public void basic_5mins_1() throws Exception {
    TimeSlice slice = new TimeSlice(cal, TimeSlice.Interval.FIVE_MINUTE);
    assertEquals(11, slice.getStamp().get(Calendar.HOUR_OF_DAY));
    assertEquals(5, slice.getStamp().get(Calendar.MINUTE));
    assertEquals(0, slice.getStamp().get(Calendar.SECOND));
    assertEquals(0, slice.getStamp().get(Calendar.MILLISECOND));
    assertEquals(3, slice.getWeekday());
    assertEquals(133, slice.getIntervalStart());
    assertEquals(134, slice.getIntervalEnd());
  }

  @Test
  public void basic_5mins_2() throws Exception {
    TimeSlice slice = new TimeSlice(1390331202L,
        TimeSlice.Interval.FIVE_MINUTE,
        Calendar.getInstance(TimeZone.getTimeZone(DEFAULT_TZ)));
    assertEquals(11, slice.getStamp().get(Calendar.HOUR_OF_DAY));
    assertEquals(5, slice.getStamp().get(Calendar.MINUTE));
    assertEquals(0, slice.getStamp().get(Calendar.SECOND));
    assertEquals(0, slice.getStamp().get(Calendar.MILLISECOND));
    assertEquals(3, slice.getWeekday());
    assertEquals(133, slice.getIntervalStart());
    assertEquals(134, slice.getIntervalEnd());
  }

  @Test
  public void basic_30mins_1() throws Exception {
    TimeSlice slice = new TimeSlice(cal, TimeSlice.Interval.THIRTY_MINUTE);
    assertEquals(11, slice.getStamp().get(Calendar.HOUR_OF_DAY));
    assertEquals(0, slice.getStamp().get(Calendar.MINUTE));
    assertEquals(0, slice.getStamp().get(Calendar.SECOND));
    assertEquals(0, slice.getStamp().get(Calendar.MILLISECOND));
    assertEquals(3, slice.getWeekday());
    assertEquals(132, slice.getIntervalStart());
    assertEquals(138, slice.getIntervalEnd());
  }

  @Test
  public void basic_30mins_2() throws Exception {
    TimeSlice slice = new TimeSlice(1390331202L,
        TimeSlice.Interval.THIRTY_MINUTE,
        Calendar.getInstance(TimeZone.getTimeZone(DEFAULT_TZ)));
    assertEquals(11, slice.getStamp().get(Calendar.HOUR_OF_DAY));
    assertEquals(0, slice.getStamp().get(Calendar.MINUTE));
    assertEquals(0, slice.getStamp().get(Calendar.SECOND));
    assertEquals(0, slice.getStamp().get(Calendar.MILLISECOND));
    assertEquals(3, slice.getWeekday());
    assertEquals(132, slice.getIntervalStart());
    assertEquals(138, slice.getIntervalEnd());
  }

  @Test
  public void basic_120mins() throws Exception {
    TimeSlice slice = new TimeSlice(cal, TimeSlice.Interval.ONE_TWENTY_MINUTE);
    assertEquals(10, slice.getStamp().get(Calendar.HOUR_OF_DAY));
    assertEquals(0, slice.getStamp().get(Calendar.MINUTE));
    assertEquals(0, slice.getStamp().get(Calendar.SECOND));
    assertEquals(0, slice.getStamp().get(Calendar.MILLISECOND));
    assertEquals(3, slice.getWeekday());
    assertEquals(120, slice.getIntervalStart());
    assertEquals(144, slice.getIntervalEnd());
  }

  @Test
  public void adding_5mins_1() throws Exception {
    TimeSlice ts = new TimeSlice(cal, TimeSlice.Interval.FIVE_MINUTE);
    TimeSlice slice = ts.shift(3);
    assertEquals(11, slice.getStamp().get(Calendar.HOUR_OF_DAY));
    assertEquals(20, slice.getStamp().get(Calendar.MINUTE));
    assertEquals(0, slice.getStamp().get(Calendar.SECOND));
    assertEquals(0, slice.getStamp().get(Calendar.MILLISECOND));
    assertEquals(3, slice.getWeekday());
    assertEquals(136, slice.getIntervalStart());
    assertEquals(137, slice.getIntervalEnd());
  }

  @Test
  public void adding_5mins_2() throws Exception {
    TimeSlice ts = new TimeSlice(cal, TimeSlice.Interval.FIVE_MINUTE);
    TimeSlice slice = ts.shift(-3);
    assertEquals(10, slice.getStamp().get(Calendar.HOUR_OF_DAY));
    assertEquals(50, slice.getStamp().get(Calendar.MINUTE));
    assertEquals(0, slice.getStamp().get(Calendar.SECOND));
    assertEquals(0, slice.getStamp().get(Calendar.MILLISECOND));
    assertEquals(3, slice.getWeekday());
    assertEquals(130, slice.getIntervalStart());
    assertEquals(131, slice.getIntervalEnd());
  }

  @Test
  public void adding_60mins_1() throws Exception {
    TimeSlice ts = new TimeSlice(cal, TimeSlice.Interval.SIXTY_MINUTE);
    // Shift 1 hour
    TimeSlice slice = ts.shift(1);
    assertEquals(12, slice.getStamp().get(Calendar.HOUR_OF_DAY));
    assertEquals(0, slice.getStamp().get(Calendar.MINUTE));
    assertEquals(0, slice.getStamp().get(Calendar.SECOND));
    assertEquals(0, slice.getStamp().get(Calendar.MILLISECOND));
    assertEquals(3, slice.getWeekday());
    assertEquals(144, slice.getIntervalStart());
    assertEquals(156, slice.getIntervalEnd());
  }
}
