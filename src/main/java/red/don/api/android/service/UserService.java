package red.don.api.android.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import red.don.api.android.entity.UserEntity;
import red.don.api.android.mapper.UserMapper;

@Service
public class UserService {

  @Autowired
  private UserMapper mapper;

  /**
   * Delete user by email.
   *
   * @param email User email.
   * @return Success or not.
   */
  public boolean delete(String email) {
    return mapper.delete("email", email);
  }

  /**
   * Modify user information, except email(primary key).
   *
   * @param email User email.
   * @param user  New user entity.
   * @return Success or not.
   */
  public boolean modify(String email, UserEntity user) {
    return mapper.update("email", email, user);
  }

  /**
   * View user info.
   *
   * @param email User email.
   * @return User entity or null.
   */
  public UserEntity view(String email) {
    return mapper.selectOne("email", email);
  }

}
