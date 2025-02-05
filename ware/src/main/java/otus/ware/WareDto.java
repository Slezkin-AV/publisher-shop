package otus.ware;

import lombok.*;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
//@EqualsAndHashCode(callSuper=true)
@ToString(callSuper = true)
public class WareDto {//extends Event {
    private Long id;
    private Long wareType;
    private Long amount;
    private String wareName;
    private Double price;
}