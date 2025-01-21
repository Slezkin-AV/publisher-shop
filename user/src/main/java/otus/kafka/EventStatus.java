package otus.kafka;

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
