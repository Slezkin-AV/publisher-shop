package otus.exception;

import lombok.Getter;
import otus.user.UserErrorType;

import java.io.Serial;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Getter
public class SrvException extends RuntimeException{
    @Serial
    private static final long serialVersionUID = 1;

    private Timestamp timestamp;
    private int code;
    private String message;
    private String path;


    public SrvException(UserErrorType err){
        super(err.toString());
        this.timestamp = Timestamp.valueOf(LocalDateTime.now());
        this.code = 404;
        this.message = err.toString();
        this.path="";
    }

    public SrvException(int code, String message, String path){
        super(message);
        this.timestamp = Timestamp.valueOf(LocalDateTime.now());
        this.code = code;
        this.message = message;
        this.path=path;
    }
}
