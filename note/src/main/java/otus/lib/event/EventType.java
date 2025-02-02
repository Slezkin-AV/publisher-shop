package otus.lib.event;

import lombok.Getter;

@Getter
public enum EventType {
    USER_CREATE("Note creating"),
    ACCOUNT_CREATE("Account creating");

    EventType(String description) {
        this.description = description;
    }
    private String description;

}
