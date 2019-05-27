package red.don.api.android.mapper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import red.don.api.android.entity.LogEntity;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LogMapperTest {

  @Autowired
  private LogMapper mapper;

  @Before
  public void before() {
    mapper.deleteAll();
    assertEquals("table `log` should be empty", 0, mapper.countAll());
  }

  @After
  public void after() {
    mapper.deleteAll();
    assertEquals("table `log` should be empty", 0, mapper.countAll());
  }

  @Test
  public void all() {
    LogEntity entity = new LogEntity("method", "uri", "user", "ip", 1);
    assertTrue("insert(LogEntity) should return true", mapper.insert(entity));
    assertNotEquals("entity.getID() should not return 0", 0, entity.getID());
    assertEquals("countAll() should return 1", 1, mapper.countAll());
  }

}
