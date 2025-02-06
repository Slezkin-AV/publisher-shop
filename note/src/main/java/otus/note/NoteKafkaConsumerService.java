package otus.note;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import otus.lib.event.Event;

@Service
@Slf4j
@RequiredArgsConstructor
public class NoteKafkaConsumerService {

    @Autowired
    private final NoteService noteService;


    @KafkaListener(topics = {"user", "billing", "order", "ware", "delivery"}, groupId = "note-group")//, errorHandler = "handleKafkaException")
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
//        log.info(event.description());
            noteService.addNote(new Note(event));
        }
    }
}
