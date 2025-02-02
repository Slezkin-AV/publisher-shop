package otus.order;

import lombok.*;
import otus.lib.event.Event;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
//@EqualsAndHashCode(callSuper=true)
@ToString(callSuper = true)
public class OrderDto {//extends Event {
    private Long id;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private OrderStatus orderStatus;
    private Long userId;
    private double amount;
}