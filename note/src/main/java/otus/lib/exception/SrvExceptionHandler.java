package otus.lib.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
//
//    @Bean
//    public KafkaListenerErrorHandler handleKafkaException() {
//        return (message, exception) -> {
//            // Логика обработки ошибки
//            return null;
//        };
//    }
}
