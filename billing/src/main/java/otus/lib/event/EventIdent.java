package otus.lib.event;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.*;

@Slf4j
@NoArgsConstructor
@Component
public class EventIdent {

    private final Map<String, Set<EventType>> types = new HashMap<>();

    public boolean addType(String ident, EventType type) {
        if (ident == null) {
            return true;
        }
        if (types.get(ident) == null || !types.get(ident).contains(type)){
            types.put(ident, new HashSet<>(Collections.singleton(type)));
            return true;
        }
        log.warn("EventIdent: ident: {} type {} duplicates", ident, type);
        return false;
    }
    public void clearIdent(String ident){
        if( ident != null){
            types.remove(ident);
        }
    }

}
