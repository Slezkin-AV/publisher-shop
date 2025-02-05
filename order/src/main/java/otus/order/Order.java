package otus.order;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import otus.lib.event.Event;

import java.sql.Timestamp;
import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@ToString(callSuper = true)
@Table(schema = "public")
//@EqualsAndHashCode(callSuper=true)
@EnableJpaAuditing
public class Order {// extends Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(updatable = false)
    @CreatedDate
    private Timestamp createdAt;

    @LastModifiedDate
    private Timestamp updatedAt;

    private OrderStatus orderStatus;

    @NonNull
    private Long userId;
    private Long amount;
    private Double sum;
    private Long wareId;

    @PrePersist
    private void onCreate(){
        updatedAt = Timestamp.valueOf(LocalDateTime.now());
        createdAt = Timestamp.valueOf(LocalDateTime.now());
        orderStatus = OrderStatus.CREATED;
    }

    @PreUpdate
    private void onUpdate(){
        updatedAt = Timestamp.valueOf(LocalDateTime.now());
    }

//    public Order(Event event){
//        super(event.getType(), event.getStatus(),
//                event.getSource(), event.getMessage(), event.getUserId(),
//                event.getAmount(), event.getOrderId());
//    }

}
