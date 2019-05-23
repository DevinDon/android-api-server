package red.don.api.android.entity;

public class CheckEntity implements Entity {

  private static final long serialVersionUID = 1L;

  /** User ID, int, primary key. */
  private long id;
  /** User email, char(64), foreign key with `user`.`email`. */
  private String user;
  /** Check in date. */
  private long date;

  /** New empty check entity. */
  public CheckEntity() {
    this(0, null, 0);
  }

  /**
   * New check entity with user email & check in date.
   *
   * @param user User email.
   * @param date Check in date.
   */
  public CheckEntity(String user, long date) {
    this(0, user, date);
  }

  /**
   * New check entity with id, user email & check in date.
   *
   * @param id   Check record id.
   * @param user User email.
   * @param date Check in date.
   */
  public CheckEntity(long id, String user, long date) {
    this.id = id;
    this.user = user;
    this.date = date;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getUser() {
    return user;
  }

  public void setUser(String user) {
    this.user = user;
  }

  public long getDate() {
    return date;
  }

  public void setDate(long date) {
    this.date = date;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + (int) (date ^ (date >>> 32));
    result = prime * result + (int) (id ^ (id >>> 32));
    result = prime * result + ((user == null) ? 0 : user.hashCode());
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
    CheckEntity other = (CheckEntity) obj;
    if (date != other.date)
      return false;
    if (id != other.id)
      return false;
    if (user == null) {
      if (other.user != null)
        return false;
    } else if (!user.equals(other.user))
      return false;
    return true;
  }

  @Override
  public String toString() {
    return "CheckEntity [date=" + date + ", id=" + id + ", user=" + user + "]";
  }

}
