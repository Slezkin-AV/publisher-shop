package otus.order;

import otus.lib.event.Event;

public interface OrderServiceInterface {
    OrderDto createOrder(Order order);
    OrderDto getOrder(Long id);
    OrderDto updateStatus(Event event);
    OrderDto closeOrder(Long id);
//    boolean checkIdem(long miliseconds);
}
