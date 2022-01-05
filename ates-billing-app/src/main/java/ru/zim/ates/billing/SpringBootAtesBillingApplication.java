package ru.zim.ates.billing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@Configuration
@EnableAutoConfiguration
@ComponentScan(basePackages = { "ru.zim.ates.billing", "ru.zim.ates.common"})
@EnableJpaRepositories(basePackages = { "ru.zim.ates.billing", "ru.zim.ates.common.standartimpl.consumer.user", "ru.zim.ates.common.messaging.consumer"})
@EntityScan(basePackages = {"ru.zim.ates.billing", "ru.zim.ates.common.standartimpl.consumer.user", "ru.zim.ates.common.messaging.consumer"})
@EnableWebSecurity
public class SpringBootAtesBillingApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootAtesBillingApplication.class, args);
	}

}
