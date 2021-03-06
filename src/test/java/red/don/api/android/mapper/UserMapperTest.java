package red.don.api.android.mapper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import red.don.api.android.entity.UserEntity;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserMapperTest {

  @Autowired
  private UserMapper mapper;

  private String email = "email@email.com";
  private String name = "name";
  private String password = "password";
  private long token = 1;
  private UserEntity entity;

  @Before
  public void before() {
    entity = new UserEntity(email, name, password, token);
    mapper.deleteAll();
    assertEquals("database should be empty before each test", 0, mapper.countAll());
  }

  @After
  public void after() {
    mapper.deleteAll();
    assertEquals("database should be empty after each test", 0, mapper.countAll());
  }

  @Test
  public void all() {
    assertTrue("insert should be successeful", mapper.insert(entity));
    assertEquals("countAll() should be 1", 1, mapper.countAll());
    assertEquals("selectWhere(String).get(0) and entity should be equal", entity,
        mapper.selectWhere("`email` = '" + email + "'").get(0));
    assertEquals("select(String, String).get(0) and entity should be equal", entity,
        mapper.select("email", email).get(0));
    assertEquals("selectOneWhere(String) and entity should be equal", entity,
        mapper.selectOneWhere("`email` = '" + email + "'"));
    assertEquals("selectOne(String, String) and entity should be equal", entity, mapper.selectOne("email", email));
    assertEquals("selectAll().get(0) and entity should be equal", entity, mapper.selectAll().get(0));
    mapper.deleteAll();
    assertEquals("countAll() should be 0 after deleteAll()", 0, mapper.countAll());
  }

  @Test
  public void insert() {
    assertTrue("insert(entity) should return true", mapper.insert(entity));
    assertEquals("countAll() should be 1 after insert(entity)", 1, mapper.countAll());
  }

  @Test
  public void delete() {
    insert();
    assertTrue("delete(field, value) should return true", mapper.delete("email", email));
    assertEquals("countAll() should be 0 after delete(field, value)", 0, mapper.countAll());
    insert();
    assertTrue("deleteWhere(where) should return true", mapper.deleteWhere("`email` = '" + email + "'"));
    assertEquals("countAll() should be 0 after deleteWhere(where)", 0, mapper.countAll());
    insert();
    mapper.deleteAll();
    assertEquals("countAll() should be 0 after countAll()", 0, mapper.countAll());
  }

  @Test
  public void update() {
    insert();
    entity.setEmail("new@email.com");
    entity.setName("new");
    entity.setPassword("new");
    entity.setToken(token);
    assertTrue("update(entity) should return true", mapper.update("email", email, entity));
    assertEquals("selectOne(String, String) should return and equal to entity", entity,
        mapper.selectOne("email", entity.getEmail()));
  }

  @Test
  public void updateWhere() {
    insert();
    entity.setEmail("new@email.com");
    entity.setName("new");
    entity.setPassword("new");
    entity.setToken(token);
    assertTrue("update(entity) should return true", mapper.updateWhere("`email` ='" + email + "'", entity));
    assertEquals("selectOne(String, String) should return and equal to entity", entity,
        mapper.selectOne("email", entity.getEmail()));
  }

  @Test
  public void select() {
    assertNull("selectOne(String, String) should be null", mapper.selectOne("email", email));
    assertNull("selectOneWhere(where) should be null", mapper.selectOneWhere("`email` = '" + email + "'"));
    assertEquals("select(String, String).size() should be 0", 0, mapper.select("email", email).size());
    assertEquals("selectWhere(String).size() should be 0", 0, mapper.selectWhere("`email` = '" + email + "'").size());
    assertEquals("selectAll().size() should be 0", 0, mapper.selectAll().size());
    insert();
    assertEquals("selectOne(String, String) should return and equal to entity", entity,
        mapper.selectOne("email", email));
    assertEquals("selectOneWhere(where) should return and equal to entity", entity,
        mapper.selectOneWhere("`email` = '" + email + "'"));
    assertEquals("select(String, String).get(0) should equal to entity", entity, mapper.select("email", email).get(0));
    assertEquals("selectWhere(String).get(0) should equal to entity", entity,
        mapper.selectWhere("`email` = '" + email + "'").get(0));
    assertEquals("selectAll().get(0) should equal to entity", entity, mapper.selectAll().get(0));
  }

  @Test
  public void count() {
    assertEquals("count(String, String) should be 0", 0, mapper.count("email", email));
    assertEquals("countWhere(String) should be 0", 0, mapper.countWhere("`email` = '" + email + "'"));
    assertEquals("countAll() should be 0", 0, mapper.countAll());
    insert();
    assertEquals("count(String, String) should be 1", 1, mapper.count("email", email));
    assertEquals("countWhere(String) should be 1", 1, mapper.countWhere("`email` = '" + email + "'"));
    assertEquals("countAll() should be 1", 1, mapper.countAll());
  }

}
