package red.don.api.android.entity;

/** Entity: User. */
public class UserEntity implements Entity {

  private static final long serialVersionUID = 1L;

  /** User email, char(64), primary key. */
  private String email;
  /** User name, char(64). */
  private String name;
  /** User password, char(64). */
  private String password;

  /** Empty user. */
  public UserEntity() {
    this(null, null, null);
  }

  /**
   * New user with email & password.
   *
   * @param email    Email.
   * @param password Password.
   */
  public UserEntity(String email, String password) {
    this(email, null, password);
  }

  /**
   * New user with email, name & password.
   *
   * @param email    Email.
   * @param name     Name.
   * @param password Password.
   */
  public UserEntity(String email, String name, String password) {
    this.email = email;
    this.name = name;
    this.password = password;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((email == null) ? 0 : email.hashCode());
    result = prime * result + ((name == null) ? 0 : name.hashCode());
    result = prime * result + ((password == null) ? 0 : password.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    UserEntity other = (UserEntity) obj;
    if (email == null) {
      if (other.email != null)
        return false;
    } else if (!email.equals(other.email))
      return false;
    if (name == null) {
      if (other.name != null)
        return false;
    } else if (!name.equals(other.name))
      return false;
    if (password == null) {
      if (other.password != null)
        return false;
    } else if (!password.equals(other.password))
      return false;
    return true;
  }

  @Override
  public String toString() {
    return "UserEntity [email=" + email + ", name=" + name + ", password=" + password + "]";
  }

}
