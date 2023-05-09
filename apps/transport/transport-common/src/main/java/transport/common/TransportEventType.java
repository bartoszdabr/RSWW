package transport.common;

public enum TransportEventType {
  CREATE("create"),
  ADD("add"),
  CANCEL("cancel");
  private final String text;

  TransportEventType(String text) {
    this.text = text;
  }

  public String getText() {
    return text;
  }
}
