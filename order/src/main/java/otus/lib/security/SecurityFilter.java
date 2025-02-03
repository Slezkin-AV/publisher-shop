package otus.lib.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.web.filter.OncePerRequestFilter;
import otus.lib.exception.SrvErrorResponce;
import otus.lib.exception.SrvException;
import otus.lib.jwt.JwtService;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class SecurityFilter extends OncePerRequestFilter {


    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String checkURI = "/order/";
        String auth = request.getHeader("Authorization");
        String uri = request.getRequestURI();
        JwtService jwtService = new JwtService();

        long id = -1L;
        boolean checkId = false;
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

//        log.info("{} : {}, ID: {}, checkId: {}, checkURI: {}, Auth: {}",
//                request.getMethod(), request.getRequestURI(), id, checkId,
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