package otus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication //(scanBasePackages = {"otus.kafka", "otus.user"})
@EnableWebMvc
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
