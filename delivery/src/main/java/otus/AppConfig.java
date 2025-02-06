package otus;

import io.micrometer.core.aop.TimedAspect;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@EnableAspectJAutoProxy
//@EnableResourceServer
public class AppConfig {
    @Bean
    public TimedAspect timedAspect(MeterRegistry registry) {
        return new TimedAspect(registry);
    }

    @Bean
    public String myTopic() {return "delivery";}

    @Bean
    public String accessSecret(){return "qBTmv4oXFFR2GwjexDJ4t6fsIUIUhhXqlktXjXdkcyygs8nPVEwMfo29VDRRepYDVV5IkIxBMzr7OEHXEHd37w==";}
}