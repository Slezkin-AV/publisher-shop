package otus.delivery;

import otus.lib.event.Event;

public interface DeliveryServiceInterface {
    DeliveryDto getDelivery(Long id);
    boolean deliverWare(Event event);
    boolean delivered(Event event);
}
