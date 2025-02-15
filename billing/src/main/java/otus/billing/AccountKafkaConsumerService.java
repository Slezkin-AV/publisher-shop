package otus.billing;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import otus.lib.event.Event;
import otus.lib.event.EventIdent;
import otus.lib.event.EventStatus;
import otus.lib.event.EventType;

import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class AccountKafkaConsumerService {

    @Autowired
    private final AccountService accountService;

    private final EventIdent eventIdent;

    @KafkaListener(topics = {"user", "order", "ware", "delivery"}, groupId = "billing-group")//, errorHandler = "handleKafkaException")
    public void listen(String message) {
        log.info("Received Message: " + message);
        Event event = null;
        try{
            ObjectMapper objectMapper = new ObjectMapper();
            event = objectMapper.readValue(message, Event.class);
            //log.info(event.description());
        }catch(JsonProcessingException ex) {
            log.error("Ошибка трансформации сообщения: {}", ex.getMessage());
        }
        assert event != null;

//        log.info(event.description());
        if( event != null && event.getType() != null && event.getStatus() != null) {
            if(event.getType() == EventType.FINAL || event.getType() == EventType.ORDER_DELIVERED || event.getType() == EventType.ORDER_CANCELED){
                eventIdent.clearIdent(event.getStrUid());
            }else {
                if (eventIdent.addType(event.getStrUid(), event.getType()) || event.getWareId() % 10 == 8) {
                    accountService.processEvent(event);
                } else {
                    log.warn("Event duplicates: " + message);
                }
            }
        }
    }
}
