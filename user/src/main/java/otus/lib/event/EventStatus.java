package otus.lib.event;

import lombok.Getter;

@Getter
public enum EventStatus {
    SUCCESS("User creating"),
    ERROR("Account creating");

    EventStatus(String description) {
        this.description = description;
    }
    private String description;
}
