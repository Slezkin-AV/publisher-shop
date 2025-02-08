package otus.lib.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Event {

    private EventType type;
    private EventStatus status;
    private String source;
    private String message;
    private Long userId;
    private Long amount;
    private Long orderId;
    private Long wareId;
    private Double sum;
    private Timestamp created;
    private Timestamp updated;
    private OrderStatus orderStatus;


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
