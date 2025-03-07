package otus.lib.event;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Slf4j
@Component
public class EventProducer {

    private static String TOPIC;

    @Autowired
    EventProducer(@Qualifier("myTopic") String myTopic) {
        TOPIC = myTopic;
    }
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public void sendMessage(Event event){//} throws JsonProcessingException {
        String message = null;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            message = objectMapper.writeValueAsString(event);
        } catch(JsonProcessingException ex) {
            log.error("Ошибка трансформации сообщения: {}", ex.getMessage());
        }
        assert message != null;
        log.info("Sending event: {}", message);
        try {
            CompletableFuture<SendResult<String, String>> result = kafkaTemplate.send(TOPIC, event.key(), message);
            log.info("Sent: {}", result.get().getRecordMetadata().offset());
        } catch (InterruptedException | ExecutionException ex) {
            log.error("Ошибка отправки: {}", ex.getMessage());
        }
    }
}
