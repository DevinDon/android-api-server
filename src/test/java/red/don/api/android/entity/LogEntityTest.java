package red.don.api.android.entity;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LogEntityTest {

  private long id = 1;
  private String method = "GET";
  private String uri = "/check/in";
  private String user = "email@email.com";
  private String ip = "0.0.0.0";
  private long time = System.currentTimeMillis();

  @Test
  public void constructorWithoutID() {
    LogEntity entity = new LogEntity(method, uri, user, ip, time);
    assertEquals(0, entity.getID());
    assertEquals(method, entity.getMethod());
    assertEquals(uri, entity.getUri());
    assertEquals(user, entity.getUser());
    assertEquals(time, entity.getTime());
  }

  @Test
  public void constructorWithAll() {
    LogEntity entity = new LogEntity(id, method, uri, user, ip, time);
    assertEquals(id, entity.getID());
    assertEquals(method, entity.getMethod());
    assertEquals(uri, entity.getUri());
    assertEquals(user, entity.getUser());
    assertEquals(time, entity.getTime());
  }

}
