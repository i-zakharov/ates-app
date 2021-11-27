package ru.zim.ates.billing.config;

import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.zim.ates.billing.service.TasksConsumer;
import ru.zim.ates.common.standartimpl.consumer.user.service.UsersConsumer;
import ru.zim.ates.common.standartimpl.consumer.user.service.UsersStreamConsumer;
import ru.zim.ates.billing.service.TestMessageConsumer;

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
        container.setQueueNames(ATES_USERS_STREAM_BILLING_QUEUE);
        container.setMessageListener(listenerAdapter);
        return container;
    }
    @Bean
    SimpleMessageListenerContainer usersQueueContainer(ConnectionFactory connectionFactory,
                    UsersMessageListenerAdapter listenerAdapter) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(ATES_USERS_BILLING_QUEUE);
        container.setMessageListener(listenerAdapter);
        return container;
    }
    @Bean
    SimpleMessageListenerContainer tasksQueueContainer(ConnectionFactory connectionFactory,
            TasksMessageListenerAdapter listenerAdapter) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(ATES_TASKS_BILLING_QUEUE);
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
    @Bean
    UsersMessageListenerAdapter usersListenerAdapter(UsersConsumer receiver) {
        return new UsersMessageListenerAdapter(receiver, "receiveMessage");
    }
    @Bean
    TasksMessageListenerAdapter tasksListenerAdapter(TasksConsumer receiver) {
        return new TasksMessageListenerAdapter(receiver, "receiveMessage");
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
    public static class TasksMessageListenerAdapter extends MessageListenerAdapter {
        public TasksMessageListenerAdapter(Object delegate, String defaultListenerMethod) {
            super(delegate, defaultListenerMethod);
        }
    }
}
