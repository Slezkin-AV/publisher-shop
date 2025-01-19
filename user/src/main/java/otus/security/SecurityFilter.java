package otus.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;
import otus.exception.SrvErrorResponce;
import otus.exception.SrvException;
import otus.jwt.JwtService;

import java.io.IOException;

//@Component
@Slf4j
@RequiredArgsConstructor
//@Order(Ordered.HIGHEST_PRECEDENCE)
public class SecurityFilter extends OncePerRequestFilter {
    private final String  checkURI = "/user/";

    @Autowired
    @Qualifier("handlerExceptionResolver")
    private HandlerExceptionResolver exceptionResolver;
//    private final UserDetailsService userDetailsService;

//    public SecurityFilter(UserDetailsService userDetailsService) {
//        this.userDetailsService = userDetailsService;
//    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String auth = request.getHeader("Authorization");
        String uri = request.getRequestURI();
        JwtService jwtService = new JwtService();

        Long id = -1L;
        Boolean checkId = false;
        try{
            if(auth != null && uri != null) {
                if (uri.matches(checkURI + ".*")) {
                    id = Long.parseLong(uri.substring(checkURI.length()));
                    checkId = jwtService.validateId(id, auth);
                    if (!checkId) {
                        throw new SrvException(401, "Unauthorized", uri);
                    }
                }
            }
            filterChain.doFilter(request, response);
//        log.info("{} : {}, ID: {}, checkId: {}, checkURI: {} Auth: {}",
//                request.getMethod(), request.getRequestURI(), id, checkId.toString(),
//                checkURI, request.getHeader("Authorization"));
        }catch(SrvException ex) {
             response.resetBuffer();
             response.setStatus(401);
             response.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
             response.getOutputStream().print(new ObjectMapper().writeValueAsString(new SrvErrorResponce(401, "Unauthorized", uri)));
             response.flushBuffer(); // marks response as committed -- if we don't do this the request will go through normally!
//             response.sendError(401, "Unauthorized");
//             response.getWriter().println( new ObjectMapper().writeValueAsString(new SrvErrorResponce(ex)));
        }
    }
}