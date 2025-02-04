package otus.lib.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Slf4j
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig  {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable) // Disable CORS
                .authorizeHttpRequests(request -> request
                        .requestMatchers("/register","/validate","/health/", "/", "/login", "/actuator/**").permitAll()
                        .requestMatchers("/user/*","/order/*","/order","/account/**","/account").permitAll()
                        .requestMatchers("/clean/*").permitAll()
                        .anyRequest().authenticated()
                )
                .httpBasic(Customizer.withDefaults())
                .sessionManagement(httpSecuritySessionManagementConfigurer -> httpSecuritySessionManagementConfigurer
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//                .securityContext(AbstractHttpConfigurer::disable)
//                .securityContext(Customizer.withDefaults())
//                .securityMatcher("/user/*")
        ;
//        http.addFilterAfter(new AuditInterceptor(), AnonymousAuthenticationFilter.class);
        http.addFilterAfter(new SecurityFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

//    @Bean
//    public PrincipalExtractor srvPrincipalExtractor() {
//        return new SrvPrincipalExtractor();
//    }

//
////                // Configures logout settings
////                .logout(logout -> logout
////                        .logoutSuccessUrl("/") // Redirects to the root URL on successful logout
////                        .invalidateHttpSession(true) // Invalidates session to clear session data
////                        .clearAuthentication(true) // Clears authentication details
////                        .deleteCookies("JSESSIONID") // Deletes the session cookie
////                );

//
//    @Bean
//    public SecurityEvaluationContextExtension securityEvaluationContextExtension() {
//        return new SecurityEvaluationContextExtension();
//    }
}
