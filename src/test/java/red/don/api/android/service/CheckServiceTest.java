package red.don.api.android.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Calendar;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import red.don.api.android.util.CalendarUtil;
import red.don.api.android.entity.UserEntity;
import red.don.api.android.mapper.CheckMapper;
import red.don.api.android.mapper.UserMapper;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CheckServiceTest {

  @Autowired
  private CheckService service;
  @Autowired
  private CheckMapper mapper;
  @Autowired
  private UserMapper userMapper;

  /** The user that exist in database. */
  private UserEntity user;
  /** The user that exist in database. */
  private UserEntity another;
  /** The user that do not exist in database. */
  private UserEntity nobody;

  @Before
  public void before() {
    user = new UserEntity("email", "name", "password");
    another = new UserEntity("another", "name", "password");
    nobody = new UserEntity("404", "404", "404");
    mapper.deleteAll();
    assertEquals("table check should be empty", 0, mapper.countAll());
    userMapper.deleteAll();
    assertEquals("table user should be empty", 0, userMapper.countAll());
    assertTrue("insert user should success", userMapper.insert(user));
    assertTrue("insert user should success", userMapper.insert(another));
  }

  @After
  public void after() {
    mapper.deleteAll();
    assertEquals("table check should be empty", 0, mapper.countAll());
    userMapper.deleteAll();
    assertEquals("table user should be empty", 0, userMapper.countAll());
  }

  @Test
  public void check() {
    assertTrue("check(UserEntity) should return true because user is not checked", service.check(user));
    assertTrue("check(UserEntity) should return true because antoher is not checked", service.check(another));
    assertTrue("check(UserEntity) should return true because user has already checked in", service.check(user));
    assertTrue("check(UserEntity) should return true because antoher has already checked in", service.check(another));
    assertFalse("check(UserEntity) should return false because user not exist", service.check(nobody));
  }

  @Test
  public void view() {
    Calendar calendar = CalendarUtil.now();
    long millisecond = calendar.getTimeInMillis();
    int year = calendar.get(Calendar.YEAR);
    int month = calendar.get(Calendar.MONTH) + 1;
    int day = calendar.get(Calendar.DAY_OF_MONTH);
    assertTrue("check(UserEntity) should return true", service.check(another)); // check in another
    assertFalse("view(UserEntity, Calendar) should return false because there is no check record",
        service.view(user, calendar));
    assertFalse("view(UserEntity, long) should return false because there is no check record",
        service.view(user, millisecond));
    assertFalse("view(UserEntity, int, int, int) should return false because there is no check record",
        service.view(user, year, month, day));
    assertTrue("check(UserEntity) should return true", service.check(user)); // check in user
    assertTrue("view(UserEntity, Calendar) should return true", service.view(user, calendar));
    assertTrue("view(UserEntity, long) should return true", service.view(user, millisecond));
    assertTrue("view(UserEntity, int, int, int) should return true", service.view(user, year, month, day));
  }

}
