package otus.ware;

import jakarta.persistence.*;
import lombok.*;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@ToString(callSuper = true)
@Table(schema = "public")
//@EqualsAndHashCode(callSuper=true)
public class Ware {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long wareType;

    @NonNull
//    @PositiveOrZero
    private Long amount;
    private String wareName;
    private Double price;
}
