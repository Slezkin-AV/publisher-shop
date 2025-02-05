package otus.lib.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.KafkaListenerErrorHandler;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;

@ControllerAdvice
public class SrvExceptionHandler {

    @ExceptionHandler({NoHandlerFoundException.class})
    public ResponseEntity<SrvErrorResponce> handleNoHandlerFoundException(
            NoHandlerFoundException ex, HttpServletRequest httpServletRequest) {
        SrvErrorResponce apiErrorResponse = new SrvErrorResponce(404, "Resource not found", "");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).contentType(MediaType.APPLICATION_JSON).body(apiErrorResponse);
    }

    @ExceptionHandler({SrvException.class})
    public ResponseEntity<SrvErrorResponce> handleUserException(
            SrvException ex, HttpServletRequest httpServletRequest) {
        SrvErrorResponce apiErrorResponse = new SrvErrorResponce(ex.getCode(), ex.getMessage(),"");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).contentType(MediaType.APPLICATION_JSON).body(apiErrorResponse);
    }

    @Bean
    public KafkaListenerErrorHandler handleKafkaException() { //(DeadLetterPublishingRecoverer recoverer) {
        return (message, exception) -> {
            // Логика обработки ошибки
            if ((message.getHeaders().get(KafkaHeaders.DELIVERY_ATTEMPT, Integer.class) != null)
                && (message.getHeaders().get(KafkaHeaders.DELIVERY_ATTEMPT, Integer.class) > 9)) {
//                recoverer.accept(message.getHeaders().get(KafkaHeaders.RAW_DATA, ConsumerRecord.class), exception);
                return "FAILED sending " + message.getHeaders();
            }
            throw exception;
        };
    }
}
