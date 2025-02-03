package otus.order;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import otus.lib.event.Event;
import otus.lib.event.EventStatus;
import otus.lib.event.EventType;

import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderKafkaConsumerService {

    @Autowired
    private final OrderService orderService;


    @KafkaListener(topics = {"user", "billing"}, groupId = "order-group", errorHandler = "handleKafkaException")
    public void listen(String message) throws JsonProcessingException {
        log.info("Received Message: " + message);
        ObjectMapper objectMapper = new ObjectMapper();
        Event event = objectMapper.readValue(message, Event.class);
//        log.info(event.description());
        // Здесь можно добавить логику обработки сообщения

        //предполагаем что ничего делать не надо
        OrderStatus orderStatus = OrderStatus.NONE;

        // проверка оплаты счета
        if (Objects.equals(event.getSource(), "billing")) {
            if (event.getType() == EventType.ACCOUNT_PAID) {
                if (event.getStatus() == EventStatus.SUCCESS) {
                    orderStatus = OrderStatus.PAID;
                } else {
                    orderStatus = OrderStatus.REJECTED;
                }
            }
        }

        //если на событие все же надо реагировать
        if (orderStatus != OrderStatus.NONE) {
            Event event1 = new Event();
            orderService.updateStatus(event.getOrderId(), orderStatus);


        }
    }
}
