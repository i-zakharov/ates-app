package ru.zim.ates.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@Configuration
@EnableAutoConfiguration
@ComponentScan(basePackages = { "ru.zim.ates.auth", "ru.zim.ates.common" })
@EnableWebSecurity
public class SpringBootAtesAuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootAtesAuthApplication.class, args);
    }

}
