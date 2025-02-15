package otus.order;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import otus.lib.event.*;
import otus.lib.exception.ErrorType;
import otus.lib.exception.SrvException;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;


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
    public OrderDto closeOrder(Long id){
        Order order = orderRepository.findById(id).orElseThrow(() -> new SrvException(ErrorType.ORD_NOT_FOUND));

        // + to Kafka
        try {
            EventType eventType = EventType.FINAL;
            Event event1 = new Event(eventType, EventStatus.SUCCESS,
                    "order",eventType.getDescription(), order.getUserId(), order.getAmount(),
                    order.getId(),order.getWareId(), order.getSum(),
                    Timestamp.valueOf(LocalDateTime.now()),
                    Timestamp.valueOf(LocalDateTime.now()), order.getOrderStatus(), order.getUid().toString()
            );
            eventProducer.sendMessage(event1);
        } catch(RuntimeException ex) {
            log.info(String.valueOf(ex));
        }

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

        //был ли такой же счет только что
        if (!checkIdemp(order, 500L)){
            throw new SrvException(ErrorType.ORD_DUPLICATE);
        }

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
                    Timestamp.valueOf(LocalDateTime.now()), order1.getOrderStatus(), order1.getUid().toString()
            );
            eventProducer.sendMessage(event1);
            if(event1.getWareId() % 10 == 8 || event1.getWareId() % 10 == 7) {
                eventProducer.sendMessage(event1);
            }
        } catch(RuntimeException ex) {
            log.info(String.valueOf(ex));
        }

        return OrderMapper.mapToOrderDto(order1);
    }

    public void cleanAll(){
        orderRepository.deleteAll();
    }

    private boolean checkIdemp(Order order, long miliseconds) {
        AtomicBoolean res = new AtomicBoolean(true);
        List<Order> orderList = orderRepository.findByHash(order.generateHash());
        if (orderList != null && !orderList.isEmpty()) {
            log.warn("checkIdemp: found {} orderList {} ", order.generateHash(), orderList.size());
            orderList.forEach((ord) -> {
                LocalDateTime start = LocalDateTime.now();
                LocalDateTime end = ord.getCreatedAt().toLocalDateTime();
                Duration timeElapsed = Duration.between(end, start);
                log.warn("start = {}", start.toString());
                log.warn("end = {}", end.toString());
                log.warn("time duration = {}", timeElapsed.toMillis());
                if (timeElapsed.toMillis() < miliseconds) {
                    res.set(false);
                }
                log.info(ord.getCreatedAt().toString(), ord.getHash());
            });
        }
        else{
            log.info("checkIdemp: order not found {} ", order.getHash());
        }
        return res.get();
    }

}
