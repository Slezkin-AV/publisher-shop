package otus.jwt;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class JwtResponce {
    private final String type = "Bearer";
    private Long UserID;
    private String accessToken;
}
