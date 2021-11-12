package ru.zim.ates.auth.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static ru.zim.ates.common.config.MqConfig.ATES_EXCHANGE_TEST;
import static ru.zim.ates.common.config.MqConfig.ATES_QUEUE_TEST;
import static ru.zim.ates.common.config.MqConfig.ATES_ROOTING_KEY_TEST;

@Configuration
public class AppRabbitMqConfig {

    @Bean
    public Queue myQueue() {
        return new Queue(ATES_QUEUE_TEST, false, false, false);
    }

    @Bean
    TopicExchange exchange() {
        return new TopicExchange(ATES_EXCHANGE_TEST, false, false);
    }

    @Bean
    Binding binding(Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(ATES_ROOTING_KEY_TEST);
    }

}
