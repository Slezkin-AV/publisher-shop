package otus.kafka;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Event {
    private final EventType type;
    private final EventStatus status;
    private final String source;
    private final String message;
//    private final long count;
//    private final double amount;

    public String description(){return "OK";};
}
