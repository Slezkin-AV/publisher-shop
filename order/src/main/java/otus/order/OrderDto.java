package otus.order;

import lombok.*;
import otus.lib.event.OrderStatus;

import java.sql.Timestamp;
import java.util.UUID;

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
    private Long amount;
    private Double sum;
    private Long wareId;
    private String orderStatusDescription;
    private String hash;
    private UUID uid;
}