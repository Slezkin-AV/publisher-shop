package otus.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import otus.exception.SrvException;
import otus.user.UserErrorType;

import java.security.Principal;
import java.util.Optional;

@Slf4j
@Component
public class SrvPrincipalExtractor{//} implements PrincipalExtractor {

//    @Override
//    public Object extractPrincipal(Map<String, Object> map) {
//        log.info("PrincipalExtractor");
//        map.forEach(((k,v) -> log.info(k, v)));
//        return map.get("name");
//    }

    public String getUser(){
        Object principal = Optional.ofNullable(SecurityContextHolder.getContext())
                .map(SecurityContext::getAuthentication)
                .map(Principal::getName)
                .orElseThrow(() -> new SrvException(UserErrorType.ERR_NOT_LOGGED));
        String username = principal.toString();
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        }
        log.info("Got principal: {}", username);
        return username;

    }
}