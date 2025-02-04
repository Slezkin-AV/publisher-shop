package otus.ware;

import otus.lib.event.Event;

public interface WareServiceInterface {
    WareDto createWare(Ware ware);
    WareTypeDto createWareType(WareType ware);
    WareDto getWare(Long id);
    WareTypeDto getWareType(Long id);
    boolean reserveWare(Event event);
}
