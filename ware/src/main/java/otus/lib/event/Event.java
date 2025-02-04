package otus.lib.event;

import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
public class Event {

    private EventType type;
    private EventStatus status;
    private String source;
    private String message;
    private Long userId;
    private Long amount;
    private Long orderId;
    private Long wareId;

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
                + "orderId='" + orderId + "'"
                + "wareId='" + wareId + "'"
                + " }";
    }
}
