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

import red.don.api.android.entity.CheckEntity;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CheckMapperTest {

  @Autowired
  private CheckMapper mapper;

  private String user = "email@email.com";
  private long date = System.currentTimeMillis();
  private CheckEntity entity;

  @Before
  public void before() {
    entity = new CheckEntity(user, date);
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
    assertEquals("select(where).get(0) and entity should be equal", entity,
        mapper.selectWhere("`id` = '" + entity.getId() + "'").get(0));
    assertEquals("select(field, value).get(0) and entity should be equal", entity,
        mapper.select("id", entity.getId()).get(0));
    assertEquals("selectOne(where) and entity should be equal", entity,
        mapper.selectOneWhere("`id` = '" + entity.getId() + "'"));
    assertEquals("selectOne(field, value) and entity should be equal", entity, mapper.selectOne("id", entity.getId()));
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
    assertTrue("delete(field, value) should return true", mapper.delete("id", entity.getId()));
    assertEquals("countAll() should be 0 after delete(field, value)", 0, mapper.countAll());
    insert();
    assertTrue("deleteWhere(where) should return true", mapper.deleteWhere("`id` = '" + entity.getId() + "'"));
    assertEquals("countAll() should be 0 after deleteWhere(where)", 0, mapper.countAll());
    insert();
    mapper.deleteAll();
    assertEquals("countAll() should be 0 after countAll()", 0, mapper.countAll());
  }

  @Test
  public void update() {
    insert();
    entity.setUser("new");
    entity.setDate(System.currentTimeMillis());
    assertTrue("update(entity) should return true", mapper.update("id", entity.getId(), entity));
    assertEquals("selectOne(field, value) should return and equal to entity", entity,
        mapper.selectOne("id", entity.getId()));
    entity.setUser("user");
    assertTrue("update(entity) should return true", mapper.updateWhere("`id` = '" + entity.getId() + "'", entity));
    assertEquals("selectOne(field, value) should return and equal to entity", entity,
        mapper.selectOne("id", entity.getId()));
  }

  @Test
  public void select() {
    assertNull("selectOne(field, value) should be null", mapper.selectOne("id", entity.getId()));
    assertNull("selectOneWhere(where) should be null", mapper.selectOneWhere("`id` = '" + entity.getId() + "'"));
    assertEquals("select(field, value).size() should be 0", 0, mapper.select("id", entity.getId()).size());
    assertEquals("selectWhere(where).size() should be 0", 0,
        mapper.selectWhere("`id` = '" + entity.getId() + "'").size());
    assertEquals("selectAll().size() should be 0", 0, mapper.selectAll().size());
    insert();
    assertEquals("selectOne(field, value) should return and equal to entity", entity,
        mapper.selectOne("id", entity.getId()));
    assertEquals("selectOneWhere(where) should return and equal to entity", entity,
        mapper.selectOneWhere("`id` = '" + entity.getId() + "'"));
    assertEquals("select(field, value).get(0) should equal to entity", entity,
        mapper.select("id", entity.getId()).get(0));
    assertEquals("selectWhere(where).get(0) should equal to entity", entity,
        mapper.selectWhere("`id` = '" + entity.getId() + "'").get(0));
    assertEquals("selectAll().get(0) should equal to entity", entity, mapper.selectAll().get(0));
  }

  @Test
  public void count() {
    assertEquals("count(field, value) should be 0", 0, mapper.count("id", entity.getId()));
    assertEquals("countWhere(where) should be 0", 0, mapper.countWhere("`id` = '" + entity.getId() + "'"));
    assertEquals("countAll() should be 0", 0, mapper.countAll());
    insert();
    assertEquals("count(field, value) should be 1", 1, mapper.count("id", entity.getId()));
    assertEquals("countWhere(where) should be 1", 1, mapper.countWhere("`id` = '" + entity.getId() + "'"));
    assertEquals("countAll() should be 1", 1, mapper.countAll());
  }

}
