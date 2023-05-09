package transport.write.messaging;

public enum ResponseMessageStatus {
  FAILURE("failure"),
  SUCCEEDED("succeeded");

  private final String text;

  ResponseMessageStatus(String text) {
    this.text = text;
  }

  public String getText() {
    return text;
  }
}
