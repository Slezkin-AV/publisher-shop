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


    @KafkaListener(topics = {"user", "billing", "order"}, groupId = "note-group")//, errorHandler = "handleKafkaException")
    public void listen(String message) throws JsonProcessingException {
        log.info("Received Message: " + message);
        ObjectMapper objectMapper = new ObjectMapper();
        Event event = objectMapper.readValue(message, Event.class);
//        log.info(event.description());
        // Здесь можно добавить логику обработки сообщения
        noteService.addNote(new Note(event));
    }
}
