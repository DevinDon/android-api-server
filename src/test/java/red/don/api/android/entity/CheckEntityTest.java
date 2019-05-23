package red.don.api.android.entity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CheckEntityTest {

  private int id = 1;
  private String user = "email@email.com";
  private long date = new Date().getTime();

  @Test
  public void constructorWithNothing() {
    CheckEntity entity = new CheckEntity();
    assertEquals("id should be 0", 0, entity.getId());
    assertNull("user should be null", entity.getUser());
    assertEquals("date should be 0", 0, entity.getDate());
  }

  @Test
  public void constructorWithUserAndDate() {
    CheckEntity entity = new CheckEntity(user, date);
    assertEquals("id should be 0", 0, entity.getId());
    assertEquals("user should be " + user, user, entity.getUser());
    assertEquals("date should be " + date, date, entity.getDate());
  }

  @Test
  public void constructorWithAll() {
    CheckEntity entity = new CheckEntity(id, user, date);
    assertEquals("id should be " + id, id, entity.getId());
    assertEquals("user should be " + user, user, entity.getUser());
    assertEquals("date should be " + date, date, entity.getDate());
  }

  @Test
  public void getterAndSetter() {
    CheckEntity entity = new CheckEntity();
    entity.setId(id);
    assertEquals("id should be " + id, id, entity.getId());
    entity.setUser(user);
    assertEquals("user should be " + user, user, entity.getUser());
    entity.setDate(date);
    assertEquals("date should be " + date, date, entity.getDate());
  }

  @Test
  public void stringify() {
    CheckEntity entity = new CheckEntity(id, user, date);
    assertEquals("stringify should work", "CheckEntity [date=" + date + ", id=" + id + ", user=" + user + "]",
        entity.toString());
  }

  @Test
  public void equalWithOther() {
    CheckEntity entity = new CheckEntity(id, user, date);
    CheckEntity another = new CheckEntity(id, user, date);
    assertEquals("these two entities shoule be equal", entity, another);
  }

}
