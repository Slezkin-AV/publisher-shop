package otus.lib.exception;

import lombok.Getter;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Getter
public class SrvErrorResponce {

    private Timestamp timestamp;
    private int code;
    private String message;
    private String path;

    public SrvErrorResponce(int code, String message, String path) {
        this.timestamp = Timestamp.valueOf(LocalDateTime.now());
        this.code = code;
        this.message = message;
        this.path=path;
    }

    public SrvErrorResponce(SrvException ex ){
        this.timestamp = Timestamp.valueOf(LocalDateTime.now());
        this.code = ex.getCode();
        this.message = ex.getMessage();
        this.path= ex.getPath();
    }

}
