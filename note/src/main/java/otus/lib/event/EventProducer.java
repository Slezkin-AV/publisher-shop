package otus.lib.event;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

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

    public void sendMessage(Event event) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String message = objectMapper.writeValueAsString(event);
        log.info("Sending event: {}", message);
        kafkaTemplate.send(TOPIC, event.key(), message);
    }
}
