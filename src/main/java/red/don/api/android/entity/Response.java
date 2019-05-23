package red.don.api.android.entity;

public class Response<T> {

  public boolean status;

  public String message;

  public T content;

  public Response(boolean status) {
    this(status, null, null);
  }

  public Response(boolean status, T content) {
    this(status, null, content);
  }

  public Response(boolean status, String message) {
    this(status, message, null);
  }

  public Response(boolean status, String message, T content) {
    this.status = status;
    this.message = message;
    this.content = content;
  }

}
