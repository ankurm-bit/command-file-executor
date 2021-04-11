package in.mozark;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CommandFileExecutorApplication {

	public static void main(String[] args) {
		System.setProperty("spring.config.name", "config");
		SpringApplication.run(CommandFileExecutorApplication.class, args);
	}

}
