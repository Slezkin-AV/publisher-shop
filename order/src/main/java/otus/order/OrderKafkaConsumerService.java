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


    @KafkaListener(topics = {"billing", "ware", "delivery"}, groupId = "order-group", errorHandler = "handleKafkaException")
    public void listen(String message) {
        log.info("Received Message: " + message);
        Event event = null;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            event = objectMapper.readValue(message, Event.class);
            //log.info(event.description());
        } catch (JsonProcessingException ex) {
            log.error("Ошибка трансформации сообщения: {}", ex.getMessage());
        }
        assert event != null;
        if (event != null && event.getStatus() != null && event.getType() != null && event.getOrderId() != null) {
            // обновляем статус по событию
            orderService.updateStatus(event);
        }
    }
}
