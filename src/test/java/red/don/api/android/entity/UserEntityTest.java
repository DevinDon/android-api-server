package red.don.api.android.entity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserEntityTest {

  private String email = "email@email.com";
  private String name = "name";
  private String password = "password";

  @Test
  public void constructorWithNothing() {
    UserEntity entity = new UserEntity();
    assertNull("email should be null", entity.getEmail());
    assertNull("name shoule be null", entity.getName());
    assertNull("password should be null", entity.getPassword());
  }

  @Test
  public void constructorWithEmailAndPassword() {
    UserEntity entity = new UserEntity(email, password);
    assertEquals("email should be " + email, email, entity.getEmail());
    assertNull("name should be null", entity.getName());
    assertEquals("password should be " + password, password, entity.getPassword());
  }

  @Test
  public void constructorWithAll() {
    UserEntity entity = new UserEntity(email, name, password);
    assertEquals("email should be " + email, email, entity.getEmail());
    assertEquals("name should be " + name, name, entity.getName());
    assertEquals("password should be " + password, password, entity.getPassword());
  }

  @Test
  public void getterAndSetter() {
    UserEntity entity = new UserEntity();
    entity.setEmail(email);
    assertEquals("email should be set", email, entity.getEmail());
    entity.setName(name);
    assertEquals("name should be set", name, entity.getName());
    entity.setPassword(password);
    assertEquals("password should be set", password, entity.getPassword());
  }

  @Test
  public void stringify() {
    UserEntity entity = new UserEntity(email, name, password);
    assertEquals("stringify should work",
        "UserEntity [email=" + email + ", name=" + name + ", password=" + password + "]", entity.toString());
  }

  @Test
  public void equalWithOther() {
    UserEntity entity = new UserEntity(email, name, password);
    UserEntity another = new UserEntity(email, name, password);
    assertEquals("these two entities should be equal", entity, another);
  }

}
