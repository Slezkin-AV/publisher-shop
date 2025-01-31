package otus.note;

import lombok.*;
import otus.lib.event.Event;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper=true)
@ToString(callSuper = true)
public class NoteDto extends Event {
    private Long id;

}