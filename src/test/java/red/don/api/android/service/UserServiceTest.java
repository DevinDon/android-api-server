package red.don.api.android.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
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
import red.don.api.android.mapper.UserMapper;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceTest {

  @Autowired
  private UserService service;
  @Autowired
  private UserMapper mapper;

  /** The user that exist in database. */
  private UserEntity user;
  /** The user that do not exist in database. */
  private UserEntity nobody;

  @Before
  public void before() {
    user = new UserEntity("email", "name", "password");
    nobody = new UserEntity("404", "404", "404");
    mapper.deleteAll();
    assertEquals("table `user` should be empty", 0, mapper.countAll());
    assertTrue("insert user should be successful", mapper.insert(user));
  }

  @After
  public void after() {
    mapper.deleteAll();
    assertEquals("table `user` should be empty", 0, mapper.countAll());
  }

  @Test
  public void delete() {
    assertTrue("delete(String) should return true because user is exist", service.delete(user.getEmail()));
    assertFalse("delete(String) should return false because user is not exist", service.delete(user.getEmail()));
  }

  @Test
  public void modify() {
    user.setName("new name");
    user.setPassword("new password");
    assertTrue("modify(String, UserEntity) should return true", service.modify(user.getEmail(), user));
    assertFalse("modify(String, UserEntity) should return false because nobody is not exist",
        service.modify(nobody.getEmail(), nobody));
    var modified = mapper.selectOne("email", user.getEmail());
    assertEquals("name should be 'new name'", "new name", modified.getName());
    assertEquals("password should be 'new password'", "new password", modified.getPassword());
  }

  @Test
  public void view() {
    assertEquals("view(String) should return & equals to user", user, service.view(user.getEmail()));
    assertNull("view(String) should return null because nobody is not exist", service.view(nobody.getEmail()));
  }

}
