package red.don.api.android.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import red.don.api.android.entity.UserEntity;
import red.don.api.android.util.JWTUtil;

@RunWith(SpringRunner.class)
@SpringBootTest
public class JWTUtilTest {

  @Test
  public void all() {
    UserEntity user = new UserEntity("email@email.com", "username", null);
    String token = JWTUtil.generate(user);
    UserEntity parse = JWTUtil.parse(token);
    assertNotNull("generate(UserEntity) should return & not null", token);
    assertEquals("parse(String) should return & equal to user", user, parse);
    assertNull("parse(String) should return null because token is invalid", JWTUtil.parse("a invalid jwt token"));
  }

}
