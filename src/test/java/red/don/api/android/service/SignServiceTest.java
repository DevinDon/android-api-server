package red.don.api.android.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
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
public class SignServiceTest {

  @Autowired
  private SignService service;
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
  public void signIn() {
    assertNotNull("signIn(UserEntity) should return token because user is exist", service.signIn(user));
    assertNull("signIn(UserEntity) should return null because email is wrong",
        service.signIn(new UserEntity("wrong email", "password")));
    assertNull("signIn(UserEntity) should return null because password is wrong",
        service.signIn(new UserEntity("email", "wrong password")));
    assertNull("signIn(UserEntity) should return null because nobody is not exist", service.signIn(nobody));
  }

  @Test
  public void signOut() {
    assertTrue("signOut(UserEntity) should return true", service.signOut(user));
    assertFalse("signOut(UserEntity) should return false because user not exist", service.signOut(nobody));
  }

  @Test
  public void signUp() {
    assertTrue("signUp(UserEntity) should return true because nobody is not exist", service.signUp(nobody));
    assertFalse("signUp(UserEntity) should return false because nobody is exist", service.signUp(nobody));
  }

}
