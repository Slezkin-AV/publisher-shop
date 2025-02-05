package otus.order;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import otus.lib.event.Event;
import otus.lib.event.EventProducer;
import otus.lib.event.EventStatus;
import otus.lib.event.EventType;
import otus.lib.exception.ErrorType;
import otus.lib.exception.SrvException;

import java.util.Objects;
import java.util.Optional;


@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService implements OrderServiceInterface {

    private final OrderRepositoryInterface orderRepository;

    @Autowired
    private final EventProducer eventProducer;

    @Override
    public OrderDto getOrder(Long id){
        Order order = orderRepository.findById(id).orElseThrow(() -> new SrvException(ErrorType.ORD_NOT_FOUND));
        return OrderMapper.mapToOrderDto(order);
    }

    @Override
    public OrderDto updateStatus(Event event){
        Order order = orderRepository.findById(event.getOrderId()).orElseThrow(() -> new SrvException(ErrorType.ORD_NOT_FOUND));

//        switch (event.getStatus()) {
//            case EventType.ACCOUNT_PAID:
//        }

        if ((event.getType() == EventType.ACCOUNT_PAID) && (event.getStatus() == EventStatus.SUCCESS)) {
            order.setOrderStatus(OrderStatus.PAID);
            event.setType(EventType.ORDER_PAID);
        }
        if ((event.getType() == EventType.ACCOUNT_PAID) && (event.getStatus() == EventStatus.ERROR)){
            order.setOrderStatus(OrderStatus.REJECTED);
            event.setType(EventType.ORDER_CANCELED);
        }

        orderRepository.save(order);

        event.setSource("order");
        event.setMessage(event.getType().getDescription());

        // + to Kafka
        eventProducer.sendMessage(event);

        return OrderMapper.mapToOrderDto(order);
    }

    @Override
    public OrderDto createOrder(Order order){
        Order order1 = null;
        try {
            order1 = orderRepository.save(order);
        }catch (RuntimeException ex){
            log.info(String.valueOf(ex));
        }
        assert order1 != null;
        // + to Kafka
        try {
            EventType eventType = EventType.ORDER_CREATED;
            Event event1 = new Event(eventType, EventStatus.SUCCESS,
                    "order",eventType.getDescription(), order1.getUserId(), order1.getAmount(), order1.getId(),null, order1.getSum());
            eventProducer.sendMessage(event1);
        } catch(RuntimeException ex) {
            log.info(String.valueOf(ex));
        }

        return OrderMapper.mapToOrderDto(order1);
    }

    public void cleanAll(){
        orderRepository.deleteAll();
    }

}
