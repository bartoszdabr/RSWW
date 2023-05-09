package transport.write.messaging;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum MessageType {
    @JsonProperty("cancel")
    CANCEL("cancel"),
    @JsonProperty("add")
    ADD("add");
    private final String text;

    MessageType(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
