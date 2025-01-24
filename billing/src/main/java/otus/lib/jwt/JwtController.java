package otus.lib.jwt;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
@AllArgsConstructor
public class JwtController {
    private JwtService jwtService;

    @GetMapping("/validate")
    public ResponseEntity<JwtInfo> validate(@RequestHeader("Authorization") String bearerToken){
//        log.info("validate");
        HttpStatus status = HttpStatus.FORBIDDEN;
        if( jwtService.validateBearerToken(bearerToken)){
            status = HttpStatus.ACCEPTED;
        }
        return new ResponseEntity<>(new JwtInfo(jwtService.getId(bearerToken)), status);
    }
}
