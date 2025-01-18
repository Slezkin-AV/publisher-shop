package otus.jwt;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class JwtInfo {
    private static String userId = "UserID";
    private Long id;
}
