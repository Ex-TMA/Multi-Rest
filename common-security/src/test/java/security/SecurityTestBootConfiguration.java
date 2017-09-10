package security;

import config.CommonConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(CommonConfig.class)
public class SecurityTestBootConfiguration {

	public static void main(String[] args) {
		SpringApplication.run(SecurityTestBootConfiguration.class, args);
	}

}