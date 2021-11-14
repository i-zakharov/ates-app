package ru.zim.ates.auth.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static ru.zim.ates.common.schemaregistry.MqConfig.ATES_TEST_EXCHANGE;
import static ru.zim.ates.common.schemaregistry.MqConfig.ATES_TEST_QUEUE;
import static ru.zim.ates.common.schemaregistry.MqConfig.ATES_TEST_ROOTING_KEY;
import static ru.zim.ates.common.schemaregistry.MqConfig.ATES_USERS_EXCHANGE;
import static ru.zim.ates.common.schemaregistry.MqConfig.ATES_USERS_QUEUE;
import static ru.zim.ates.common.schemaregistry.MqConfig.ATES_USERS_STREAM_EXCHANGE;
import static ru.zim.ates.common.schemaregistry.MqConfig.ATES_USERS_STREAM_QUEUE;

@Configuration
public class AppRabbitMqConfig {

    @Bean
    public TestQueue myQueue() {
        return new TestQueue(ATES_TEST_QUEUE, false, false, false);
    }

    @Bean
    TestTopicExcange exchange() {
        return new TestTopicExcange(ATES_TEST_EXCHANGE, false, false);
    }

    @Bean
    Binding binding(TestQueue queue, TestTopicExcange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(ATES_TEST_ROOTING_KEY);
    }


    @Bean
    UsersStreamTopicExcange usersStreamTopicExchange() {
        return new UsersStreamTopicExcange(ATES_USERS_STREAM_EXCHANGE, false, false);
    }

    @Bean
    public UsersStreamQueue usersStreamQueue() {
        return new UsersStreamQueue(ATES_USERS_STREAM_QUEUE, false, false, false);
    }

    @Bean
    Binding usersStreamBinding(UsersStreamQueue queue, UsersStreamTopicExcange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with("");
    }

    @Bean
    UsersTopicExcange usersTopicExchange() {
        return new UsersTopicExcange(ATES_USERS_EXCHANGE, false, false);
    }

    @Bean
    public UsersQueue usersQueue() {
        return new UsersQueue(ATES_USERS_QUEUE, false, false, false);
    }

    @Bean
    Binding usersBinding(UsersQueue queue, UsersTopicExcange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with("");
    }


    public static class UsersQueue extends Queue {
        public UsersQueue(String name, boolean durable, boolean exclusive, boolean autoDelete) {
            super(name, durable, exclusive, autoDelete);
        }
    }

    public static class UsersStreamQueue extends Queue {
        public UsersStreamQueue(String name, boolean durable, boolean exclusive, boolean autoDelete) {
            super(name, durable, exclusive, autoDelete);
        }
    }

    public static class TestQueue extends Queue {
        public TestQueue(String name, boolean durable, boolean exclusive, boolean autoDelete) {
            super(name, durable, exclusive, autoDelete);
        }
    }

    public static class UsersTopicExcange extends TopicExchange {
        public UsersTopicExcange(String name, boolean durable, boolean autoDelete) {
            super(name, durable, autoDelete);
        }

    }

    public static class UsersStreamTopicExcange extends TopicExchange {
        public UsersStreamTopicExcange(String name, boolean durable, boolean autoDelete) {
            super(name, durable, autoDelete);
        }

    }

    public static class TestTopicExcange extends TopicExchange {
        public TestTopicExcange(String name, boolean durable, boolean autoDelete) {
            super(name, durable, autoDelete);
        }

    }


}
