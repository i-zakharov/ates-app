package ru.zim.ates.tasktracker.config;

import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.zim.ates.tasktracker.service.TestMessageConsumer;
import ru.zim.ates.tasktracker.service.UsersStreamConsumer;

import static ru.zim.ates.common.schemaregistry.MqConfig.*;

@Configuration
public class AppRabbitMqConfig {
    @Bean
    SimpleMessageListenerContainer container(ConnectionFactory connectionFactory,
            TestMessageMessageListenerAdapter listenerAdapter) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(ATES_TEST_QUEUE);
        container.setMessageListener(listenerAdapter);
        return container;
    }
    @Bean
    SimpleMessageListenerContainer usersStreamQueueContainer(ConnectionFactory connectionFactory,
            UsersStreamMessageListenerAdapter listenerAdapter) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(ATES_USERS_STREAM_QUEUE);
        container.setMessageListener(listenerAdapter);
        return container;
    }

    @Bean
    TestMessageMessageListenerAdapter testMessageListenerAdapter(TestMessageConsumer receiver) {
        return new TestMessageMessageListenerAdapter(receiver, "receiveMessage");
    }
    @Bean
    UsersStreamMessageListenerAdapter usersStreamListenerAdapter(UsersStreamConsumer receiver) {
        return new UsersStreamMessageListenerAdapter(receiver, "receiveMessage");
    }


    public static class TestMessageMessageListenerAdapter extends MessageListenerAdapter {
        public TestMessageMessageListenerAdapter(Object delegate, String defaultListenerMethod) {
            super(delegate, defaultListenerMethod);
        }
    }
    public static class UsersMessageListenerAdapter extends MessageListenerAdapter {
        public UsersMessageListenerAdapter(Object delegate, String defaultListenerMethod) {
            super(delegate, defaultListenerMethod);
        }
    }
    public static class UsersStreamMessageListenerAdapter extends MessageListenerAdapter {
        public UsersStreamMessageListenerAdapter(Object delegate, String defaultListenerMethod) {
            super(delegate, defaultListenerMethod);
        }
    }
}
