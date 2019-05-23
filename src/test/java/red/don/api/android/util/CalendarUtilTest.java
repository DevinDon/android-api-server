package red.don.api.android.util;

import static org.junit.Assert.assertEquals;

import java.util.Calendar;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import red.don.api.android.util.CalendarUtil;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CalendarUtilTest {

  private Calendar date;

  @Before
  public void before() {
    date = Calendar.getInstance();
  }

  @Test
  public void setToDayOfStart() {
    date = CalendarUtil.setToDayOfStart(date);
    assertEquals("Hour should be 0", 0, date.get(Calendar.HOUR));
    assertEquals("Minute should be 0", 0, date.get(Calendar.MINUTE));
    assertEquals("Second should be 0", 0, date.get(Calendar.SECOND));
  }

  @Test
  public void whichDay() {
    int year = 2000;
    int month = 10;
    int day = 1;
    var calendar = CalendarUtil.whichDay(year, month, day);
    assertEquals("Year should be " + year, year, calendar.get(Calendar.YEAR));
    assertEquals("Month should be " + month, month, calendar.get(Calendar.MONTH) + 1);
    assertEquals("Day should be " + day, day, calendar.get(Calendar.DATE));
  }

  @Test
  public void now() {
    assertEquals("now() should return & equal to Calendar.getInstance()", Calendar.getInstance(), CalendarUtil.now());
  }

  @Test
  public void today() {
    var today = CalendarUtil.today();
    int year = date.get(Calendar.YEAR);
    int month = date.get(Calendar.MONTH);
    int day = date.get(Calendar.DATE);
    assertEquals("Year should be " + year, year, today.get(Calendar.YEAR));
    assertEquals("Month should be " + month, month, today.get(Calendar.MONTH));
    assertEquals("Day should be " + day, day, today.get(Calendar.DATE));
    assertEquals("Hour should be 0", 0, today.get(Calendar.HOUR));
    assertEquals("Minute should be 0", 0, today.get(Calendar.MINUTE));
    assertEquals("Second should be 0", 0, today.get(Calendar.SECOND));
  }

  @Test
  public void tomorrow() {
    var tomorrow = CalendarUtil.tomorrow();
    date.add(Calendar.DATE, 1);
    int year = date.get(Calendar.YEAR);
    int month = date.get(Calendar.MONTH);
    int day = date.get(Calendar.DATE);
    assertEquals("Year should be " + year, year, tomorrow.get(Calendar.YEAR));
    assertEquals("Month should be " + month, month, tomorrow.get(Calendar.MONTH));
    assertEquals("Day should be " + day, day, tomorrow.get(Calendar.DATE));
    assertEquals("Hour should be 0", 0, tomorrow.get(Calendar.HOUR));
    assertEquals("Minute should be 0", 0, tomorrow.get(Calendar.MINUTE));
    assertEquals("Second should be 0", 0, tomorrow.get(Calendar.SECOND));
  }

  @Test
  public void yesterday() {
    var yesterday = CalendarUtil.yesterday();
    date.add(Calendar.DATE, -1);
    int year = date.get(Calendar.YEAR);
    int month = date.get(Calendar.MONTH);
    int day = date.get(Calendar.DATE);
    assertEquals("Year should be " + year, year, yesterday.get(Calendar.YEAR));
    assertEquals("Month should be " + month, month, yesterday.get(Calendar.MONTH));
    assertEquals("Day should be " + day, day, yesterday.get(Calendar.DATE));
    assertEquals("Hour should be 0", 0, yesterday.get(Calendar.HOUR));
    assertEquals("Minute should be 0", 0, yesterday.get(Calendar.MINUTE));
    assertEquals("Second should be 0", 0, yesterday.get(Calendar.SECOND));
  }

}
