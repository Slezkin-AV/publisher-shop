package otus.lib.event;

import lombok.Getter;

@Getter
public enum EventStatus {
    SUCCESS("SUCCESS"),
    ERROR("ERROR");

    EventStatus(String description) {
        this.description = description;
    }
    private String description;
}
