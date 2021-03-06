package red.don.api.android.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import red.don.api.android.mapper.LogMapper;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LogServiceTest {

  @Autowired
  private LogMapper mapper;

  @Autowired
  private LogService service;

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
    long count = service.getCount();
    assertNotEquals("count should not be 0", 0, count);
  }

}
