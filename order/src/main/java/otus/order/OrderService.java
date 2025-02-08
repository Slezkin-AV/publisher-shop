package otus.order;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import otus.lib.event.*;
import otus.lib.exception.ErrorType;
import otus.lib.exception.SrvException;

import java.sql.Timestamp;
import java.time.LocalDateTime;



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
        OrderStatus orderStatus = OrderStatus.NONE;
        EventType eventType = EventType.NONE;
//        EventStatus eventStatus = EventStatus.ERROR;

        switch (event.getType()) {
            case ACCOUNT_PAID -> {//from "billing"
                if(event.getStatus() == EventStatus.SUCCESS) {
                    eventType = EventType.ORDER_PAID;
                    orderStatus = OrderStatus.PAID;
                }else {
                    orderStatus = OrderStatus.REJECTED;
                    eventType = EventType.ORDER_CANCELED;
                }
            }
            case ACCOUNT_RETURN -> {
                //from "billing"
                eventType = EventType.ORDER_CANCELED;
                if(event.getStatus() == EventStatus.SUCCESS) {
                    orderStatus = OrderStatus.PAYBACK;
                }else {orderStatus = OrderStatus.REJECTED;}
            }
            case RESERVE_CREATING -> { //from "ware"
                if (event.getStatus() == EventStatus.SUCCESS) { //есть порох...
                    orderStatus = OrderStatus.RESERVED;
                    eventType = EventType.ORDER_RESERVED;
                } else { //или уже на складе ничего нет
                    orderStatus = OrderStatus.REJECTED;
                    eventType = EventType.ORDER_CANCELED;
                }
            }
            case DELIVERING -> {//from "delivery"
                if (event.getStatus() == EventStatus.SUCCESS) {//доставка состоялась...
                    orderStatus = OrderStatus.DELIVERED;
                    eventType = EventType.ORDER_DELIVERED;
                } else {//... или не состоялась
                    orderStatus = OrderStatus.REJECTED;
                    eventType = EventType.ORDER_CANCELED;
                }
            }
            default -> {}
        }

        if(orderStatus != OrderStatus.NONE) {
            event.setSource("order");
            if(eventType.getValue() >= event.getType().getValue()) {
                order.setOrderStatus(orderStatus);
                orderRepository.save(order);
                event.setUpdated(Timestamp.valueOf(LocalDateTime.now()));
            }
            event.setType(eventType);
            event.setOrderStatus(orderStatus);
            event.setMessage(event.getType().getDescription());

            // + to Kafka
            eventProducer.sendMessage(event);
        }

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
                    "order",eventType.getDescription(), order1.getUserId(), order1.getAmount(),
                    order1.getId(),order1.getWareId(), order1.getSum(),
                    Timestamp.valueOf(LocalDateTime.now()),
                    Timestamp.valueOf(LocalDateTime.now()), order1.getOrderStatus()
            );
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
