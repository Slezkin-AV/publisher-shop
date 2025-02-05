package otus.ware;

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
public class WareKafkaConsumerService {

    @Autowired
    private final WareService wareService;


    @KafkaListener(topics = {/*"user",*/"billing","order"}, groupId = "ware-group", errorHandler = "handleKafkaException")
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

        if( event != null) {
            // проверка оплаты счета
            if (Objects.equals(event.getSource(), "billing") && (event.getStatus() == EventStatus.SUCCESS)) {
                if (event.getType() == EventType.ACCOUNT_PAID) {

                    // резервируем товар, если счет оплачен
                    // если резервирование успешно - там и передаем в доставку
                    // иначе - отменяем и оплату, и счет в целом
                    boolean reserved = wareService.reserveWare(event);
                }
            }
        }
    }

}
