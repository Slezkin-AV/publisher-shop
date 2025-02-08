package otus.delivery;

import lombok.*;
import otus.lib.event.Event;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper=true)
@ToString(callSuper = true)
public class DeliveryDto extends Event {
    private Long id;
    private String statusDescription;

}