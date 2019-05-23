package red.don.api.android.util;

import java.util.Calendar;

public class CalendarUtil {

  /**
   * Generate a new Calendar & set time to <code>00:00:00</code> .
   *
   * @param calendar Calendar.
   * @return Calendar.
   */
  public static Calendar setToDayOfStart(Calendar calendar) {
    calendar.setTimeInMillis(calendar.getTimeInMillis());
    calendar.set(Calendar.HOUR_OF_DAY, 0);
    calendar.set(Calendar.MINUTE, 0);
    calendar.set(Calendar.SECOND, 0);
    calendar.set(Calendar.MILLISECOND, 0);
    return calendar;
  }

  /**
   * Generate a new Calendar & set date to <code>year-month-day</code> .
   *
   * @param year  Year.
   * @param month Month, 1 is January.
   * @param day   Day.
   * @return Calendar.
   */
  public static Calendar whichDay(int year, int month, int day) {
    var calendar = Calendar.getInstance();
    calendar.clear();
    calendar.set(year, month - 1, day);
    return calendar;
  }

  /**
   * Calendar.getInstance().
   *
   * @return Calendar.
   */
  public static Calendar now() {
    return Calendar.getInstance();
  }

  /**
   * Calendar.getInstance() & set time to <code>00:00:00</code> .
   *
   * @return Calendar.
   */
  public static Calendar today() {
    return setToDayOfStart(now());
  }

  /**
   * Generate a new calendar & set date to tomorrow & set time to
   * <code>00:00:00</code> .
   *
   * @return Calendar.
   */
  public static Calendar tomorrow() {
    var calendar = today();
    calendar.add(Calendar.DATE, 1);
    return calendar;
  }

  /**
   * Generate a new calendar & set date to yesterday & set time to
   * <code>00:00:00</code> .
   *
   * @return Calendar.
   */
  public static Calendar yesterday() {
    var calendar = today();
    calendar.add(Calendar.DATE, -1);
    return calendar;
  }

}
