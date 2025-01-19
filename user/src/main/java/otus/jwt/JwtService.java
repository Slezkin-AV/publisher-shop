package otus.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import otus.user.UserPub;

import javax.crypto.SecretKey;
import java.security.Key;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Map;


@Slf4j
@Component
public class JwtService {

    private final String bearer = "Bearer ";
    @Value("${jwt.secret.access}")
    private String jwtAccessSecretCoded;

    private static SecretKey jwtAccessSecret;

    public JwtService() {
        jwtAccessSecretCoded="qBTmv4oXFFR2GwjexDJ4t6fsIUIUhhXqlktXjXdkcyygs8nPVEwMfo29VDRRepYDVV5IkIxBMzr7OEHXEHd37w==";
        jwtAccessSecret = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtAccessSecretCoded));
    }

    public String generateAccessToken(@NonNull UserPub user) {
        final LocalDateTime now = LocalDateTime.now();
        final Instant accessExpirationInstant = now.plusMinutes(5).atZone(ZoneId.systemDefault()).toInstant();
        final Date accessExpiration = Date.from(accessExpirationInstant);
        final Date issued = Date.from(now.atZone(ZoneId.systemDefault()).toInstant());
        return Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setHeaderParam("alg","HS256")
                .setSubject(user.getLogin())
                .setIssuedAt(issued)
                .setExpiration(accessExpiration)
                .signWith(jwtAccessSecret)
                .claim("id", user.getId())
                .claim("login", user.getLogin())
                .claim("firstName", user.getFirstName())
                .compact();

    }

    public boolean validateBearerToken(@NonNull String bearerToken) {
        return validateToken(bearerToken.substring(bearer.length()), jwtAccessSecret);
    }

    public boolean validateAccessToken(@NonNull String accessToken) {
        return validateToken(accessToken, jwtAccessSecret);
    }

    private boolean validateToken(@NonNull String token, @NonNull Key secret) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(secret)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException expEx) {
            log.error("Token expired");
//            log.error("Token expired", expEx);
        } catch (UnsupportedJwtException unsEx) {
            log.error("Unsupported jwt");//, unsEx);
        } catch (MalformedJwtException mjEx) {
            log.error("Malformed jwt");//, mjEx);
//        } catch (SignatureException sEx) {
//            log.error("Invalid signature", sEx);
        } catch (Exception e) {
            log.error("invalid token");//, e);
        }
        return false;
    }

    public Claims getAccessClaims(@NonNull String token) {
        return getClaims(token, jwtAccessSecret);
    }

    private Claims getClaims(@NonNull String token, @NonNull Key secret) {
//        log.info("getClaims");
        return Jwts.parserBuilder()
                .setSigningKey(secret)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean validateId (Long id, String bearerToken){
        return (id == getId(bearerToken));
    }

    public Long getId(String bearerToken){
//        log.info("getId");
        Map<String,Object> claims = getAccessClaims(bearerToken.substring(bearer.length()));
        //claims.forEach((k,v) -> log.info(k + ": " + v));
        Object obj = claims.get("id");
        Long id = -1L;
        if( obj != null ){
            if( obj instanceof Long)  id =  (long) obj;
            else id = Long.parseLong(obj.toString());
        }
//        log.info("getId: " + id.toString());
        return id;
    }
}
