package red.don.api.android.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import red.don.api.android.entity.UserEntity;
import red.don.api.android.mapper.UserMapper;

@Service
public class SignService {

  @Autowired
  private UserMapper mapper;

  /**
   * Sign in.
   * 
   * @param user User entity.
   * @return Success or not.
   */
  public boolean signIn(UserEntity user) {
    return mapper.countWhere("`email` = '" + user.getEmail() + "' AND `password` = '" + user.getPassword() + "'") != 0;
  }

  /**
   * Sign out, empty method.
   * 
   * @param user User entity.
   * @return Success or not.
   */
  public boolean signOut(UserEntity user) {
    return false;
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
