package otus.order;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import otus.lib.event.Event;

import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@ToString(callSuper = true)
@Table(schema = "public")
@EqualsAndHashCode(callSuper=true)
@EnableJpaAuditing
public class Order extends Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(updatable = false)
    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    private OrderStatus orderStatus;

//    public Order(Event event){
//        super(event.getType(), event.getStatus(),
//                event.getSource(), event.getMessage(), event.getUserId(),
//                event.getAmount(), event.getOrderId());
//    }

}
