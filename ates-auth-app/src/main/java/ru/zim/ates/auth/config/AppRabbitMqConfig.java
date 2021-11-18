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
import static ru.zim.ates.common.schemaregistry.MqConfig.ATES_USERS_BILLING_QUEUE;
import static ru.zim.ates.common.schemaregistry.MqConfig.ATES_USERS_EXCHANGE;
import static ru.zim.ates.common.schemaregistry.MqConfig.ATES_USERS_STREAM_BILLING_QUEUE;
import static ru.zim.ates.common.schemaregistry.MqConfig.ATES_USERS_STREAM_EXCHANGE;
import static ru.zim.ates.common.schemaregistry.MqConfig.ATES_USERS_STREAM_TASK_TRACKER_QUEUE;
import static ru.zim.ates.common.schemaregistry.MqConfig.ATES_USERS_TASK_TRACKER_QUEUE;

@Configuration
public class AppRabbitMqConfig {

    /*
     * Topics
     */
    @Bean
    TestTopicExcange exchange() {
        return new TestTopicExcange(ATES_TEST_EXCHANGE, false, false);
    }
    @Bean
    UsersTopicExcange usersTopicExchange() {
        return new UsersTopicExcange(ATES_USERS_EXCHANGE, false, false);
    }
    @Bean
    UsersStreamTopicExcange usersStreamTopicExchange() {
        return new UsersStreamTopicExcange(ATES_USERS_STREAM_EXCHANGE, false, false);
    }

    /*
     *  Queues
     */
    @Bean
    public TestQueue myQueue() {
        return new TestQueue(ATES_TEST_QUEUE, false, false, false);
    }
    @Bean
    public UsersBillingQueue usersBillingQueue() {
        return new UsersBillingQueue(ATES_USERS_BILLING_QUEUE, false, false, false);
    }
    @Bean
    public UsersStreamBillingQueue usersStreamBillingQueue() {
        return new UsersStreamBillingQueue(ATES_USERS_STREAM_BILLING_QUEUE, false, false, false);
    }
    @Bean
    public UsersTaskTrackerQueue usersTaskTrackerQueue() {
        return new UsersTaskTrackerQueue(ATES_USERS_TASK_TRACKER_QUEUE, false, false, false);
    }
    @Bean
    public UsersStreamTaskTrackerQueue usersStreamTaskTrackerQueue() {
        return new UsersStreamTaskTrackerQueue(ATES_USERS_STREAM_TASK_TRACKER_QUEUE, false, false, false);
    }


    /*
     *  Bindings
     */
    @Bean
    Binding binding(TestQueue queue, TestTopicExcange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(ATES_TEST_ROOTING_KEY);
    }

    @Bean
    Binding usersBillingBinding(UsersBillingQueue queue, UsersTopicExcange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with("");
    }
    @Bean
    Binding usersStreamBillingBinding(UsersStreamBillingQueue queue, UsersStreamTopicExcange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with("");
    }
    @Bean
    Binding usersTaskTrackerBinding(UsersTaskTrackerQueue queue, UsersTopicExcange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with("");
    }
    @Bean
    Binding usersStreamTaskTrackerBinding(UsersStreamTaskTrackerQueue queue, UsersStreamTopicExcange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with("");
    }



    /**
     * Topics classes
     */
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

    /**
     * Queue classes
     */
    public static class UsersTaskTrackerQueue extends Queue {
        public UsersTaskTrackerQueue(String name, boolean durable, boolean exclusive, boolean autoDelete) {
            super(name, durable, exclusive, autoDelete);
        }
    }
    public static class UsersBillingQueue extends Queue {
        public UsersBillingQueue(String name, boolean durable, boolean exclusive, boolean autoDelete) {
            super(name, durable, exclusive, autoDelete);
        }
    }
    public static class UsersStreamTaskTrackerQueue extends Queue {
        public UsersStreamTaskTrackerQueue(String name, boolean durable, boolean exclusive, boolean autoDelete) {
            super(name, durable, exclusive, autoDelete);
        }
    }
    public static class UsersStreamBillingQueue extends Queue {
        public UsersStreamBillingQueue(String name, boolean durable, boolean exclusive, boolean autoDelete) {
            super(name, durable, exclusive, autoDelete);
        }
    }
    public static class TestQueue extends Queue {
        public TestQueue(String name, boolean durable, boolean exclusive, boolean autoDelete) {
            super(name, durable, exclusive, autoDelete);
        }
    }


}
