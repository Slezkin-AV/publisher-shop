package otus.lib.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Event {

    private EventType type;
    private EventStatus status;
    private String source;
    private String message;
    private long userId;
    private double amount;
    private long orderId;

    public String key(){
        return message;
    }

    public String description() {
        return "Event{ "
                + "type='" + type.getDescription() + "', "
                + "status='" + status.getDescription() + "'"
                + "source='" + source + "'"
                + "userId='" + userId + "'"
                + "amount='" + amount + "'"
                + " }";
    }
}
