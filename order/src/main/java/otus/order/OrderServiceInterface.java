package otus.order;

import otus.lib.event.EventType;

public interface OrderServiceInterface {
    OrderDto createOrder(Order order);
    OrderDto getOrder(Long id);
    OrderDto updateStatus(Long id, OrderStatus orderStatus);
}
