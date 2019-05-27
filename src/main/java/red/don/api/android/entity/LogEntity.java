package red.don.api.android.entity;

public class LogEntity implements Entity {

  private static final long serialVersionUID = 1L;

  /** Log ID, bigint, primary key. */
  private long id;
  /** Method, char(16). */
  private String method;
  /** URI, varchar(8096). */
  private String uri;
  /** User email, char(64), foreign key with `user`.`email`, nullable. */
  private String user;
  /** User IP, char(64). */
  private String ip;
  /** Millisecond time, bigint. */
  private long time;

  public LogEntity(String method, String uri, String user, String ip, long time) {
    this(0, method, uri, user, ip, time);
  }

  public LogEntity(long id, String method, String uri, String user, String ip, long time) {
    this.id = id;
    this.method = method;
    this.uri = uri;
    this.user = user;
    this.ip = ip;
    this.time = time;
  }

  public long getID() {
    return id;
  }

  public void setID(long id) {
    this.id = id;
  }

  public String getMethod() {
    return method;
  }

  public void setMethod(String method) {
    this.method = method;
  }

  public String getUri() {
    return uri;
  }

  public void setUri(String uri) {
    this.uri = uri;
  }

  public String getUser() {
    return user;
  }

  public void setUser(String user) {
    this.user = user;
  }

  public String getIP() {
    return ip;
  }

  public void setIP(String ip) {
    this.ip = ip;
  }

  public long getTime() {
    return time;
  }

  public void setTime(long time) {
    this.time = time;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + (int) (id ^ (id >>> 32));
    result = prime * result + ((ip == null) ? 0 : ip.hashCode());
    result = prime * result + ((method == null) ? 0 : method.hashCode());
    result = prime * result + (int) (time ^ (time >>> 32));
    result = prime * result + ((uri == null) ? 0 : uri.hashCode());
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
    LogEntity other = (LogEntity) obj;
    if (id != other.id)
      return false;
    if (ip == null) {
      if (other.ip != null)
        return false;
    } else if (!ip.equals(other.ip))
      return false;
    if (method == null) {
      if (other.method != null)
        return false;
    } else if (!method.equals(other.method))
      return false;
    if (time != other.time)
      return false;
    if (uri == null) {
      if (other.uri != null)
        return false;
    } else if (!uri.equals(other.uri))
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
    return "LogEntity [id=" + id + ", ip=" + ip + ", method=" + method + ", time=" + time + ", uri=" + uri + ", user="
        + user + "]";
  }

}
