package otus.ware;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
//@EqualsAndHashCode(callSuper=true)
@ToString(callSuper = true)
public class WareTypeDto {//extends Event {
    private Long id;
    private String wareName;
    private double price;
}