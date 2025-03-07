package otus.note;

import jakarta.persistence.*;
import lombok.*;
import otus.lib.event.Event;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@ToString(callSuper = true)
@Table(schema = "public")
@EqualsAndHashCode(callSuper=true)
public class Note extends Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String statusDescription;
    private String orderStatusDescription;

    public Note(Event event){
        super(event.getType(), event.getStatus(),
                event.getSource(), event.getMessage(), event.getUserId(),
                event.getAmount(), event.getOrderId(),event.getWareId(), event.getSum(),
                event.getCreated(), event.getUpdated(),event.getOrderStatus(), event.getStrUid());
        this.statusDescription = getStatus().getDescription();
        this.orderStatusDescription = getOrderStatus().getDescription();
    }

}
