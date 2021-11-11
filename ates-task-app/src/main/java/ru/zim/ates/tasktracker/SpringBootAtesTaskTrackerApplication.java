package ru.zim.ates.tasktracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@Configuration
@EnableAutoConfiguration
@ComponentScan
@EnableWebSecurity
public class SpringBootAtesTaskTrackerApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootAtesTaskTrackerApplication.class, args);
	}

}
