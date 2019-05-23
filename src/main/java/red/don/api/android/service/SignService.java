package red.don.api.android.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import red.don.api.android.entity.UserEntity;
import red.don.api.android.mapper.UserMapper;
import red.don.api.android.util.JWTUtil;

@Service
public class SignService {

  @Autowired
  private UserMapper mapper;

  /**
   * Sign in and return JWT token.
   *
   * @param user User entity.
   * @return JWT token or null.
   */
  public String signIn(UserEntity user) {
    UserEntity result = mapper
        .selectOneWhere("`email` = '" + user.getEmail() + "' AND `password` = '" + user.getPassword() + "'");
    if (result == null) {
      return null;
    } else {
      result.setToken(System.currentTimeMillis());
      mapper.update("email", result.getEmail(), result);
      return JWTUtil.generate(result);
    }
  }

  /**
   * Sign out, set token to 0 in order to disable JWT token.
   *
   * @param user User entity.
   * @return Success or not.
   */
  public boolean signOut(UserEntity user) {
    user.setToken(0);
    return mapper.update("email", user.getEmail(), user);
  }

  /**
   * Sign up.
   *
   * @param user User entity.
   * @return Success or not.
   */
  public boolean signUp(UserEntity user) {
    if (mapper.count("email", user.getEmail()) == 0) {
      return mapper.insert(user);
    } else {
      return false;
    }
  }

}
