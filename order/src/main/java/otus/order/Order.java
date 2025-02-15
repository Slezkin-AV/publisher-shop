package otus.order;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import otus.lib.event.OrderStatus;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.UUID;


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
    private String orderStatusDescription;

    @Column(name = "hash")
    private String hash;
    private UUID uid = java.util.UUID.randomUUID();

    @PrePersist
    private void onCreate(){
        updatedAt = Timestamp.valueOf(LocalDateTime.now());
        createdAt = Timestamp.valueOf(LocalDateTime.now());
        orderStatus = OrderStatus.CREATED;
        hash = generateHash();
    }

    @PreUpdate
    private void onUpdate(){
        updatedAt = Timestamp.valueOf(LocalDateTime.now());
        setOrderStatusDescription(orderStatus.getDescription());
//        orderStatusDescription = orderStatus.getDescription();
    }
    public String generateHash(){
        String myHash = null;
        final String base = String.valueOf(userId) + String.valueOf(amount) + String.valueOf(sum) + String.valueOf(wareId);
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(base.getBytes(StandardCharsets.UTF_8));
            myHash = Base64.getEncoder().encodeToString(hash).toUpperCase();
        }catch (NoSuchAlgorithmException ignored) {}
        return myHash;
    }
}
