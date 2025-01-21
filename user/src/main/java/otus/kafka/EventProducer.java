package otus.kafka;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;


@AllArgsConstructor
@Component
public class EventProducer {

    private static final String TOPIC = "user";

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public void sendMessage(String key, Event event) {
        kafkaTemplate.send(TOPIC,key, event.description());
        System.out.println("Produced message: " + event.description());
    }
}
