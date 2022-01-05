package ru.zim.ates.auth.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

import static ru.zim.ates.common.messaging.config.MqConfig.ATES_TASKS_BILLING_QUEUE;
import static ru.zim.ates.common.messaging.config.MqConfig.ATES_TASKS_EXCHANGE;
import static ru.zim.ates.common.messaging.config.MqConfig.ATES_TASKS_PRICES_EXCHANGE;
import static ru.zim.ates.common.messaging.config.MqConfig.ATES_TASKS_PRICES_TASK_TRACKER_QUEUE;
import static ru.zim.ates.common.messaging.config.MqConfig.ATES_TASKS_STREAM_BILLING_QUEUE;
import static ru.zim.ates.common.messaging.config.MqConfig.ATES_TASKS_STREAM_EXCHANGE;
import static ru.zim.ates.common.messaging.config.MqConfig.ATES_TEST_EXCHANGE;
import static ru.zim.ates.common.messaging.config.MqConfig.ATES_TEST_QUEUE;
import static ru.zim.ates.common.messaging.config.MqConfig.ATES_TEST_ROOTING_KEY;
import static ru.zim.ates.common.messaging.config.MqConfig.ATES_USERS_BILLING_QUEUE;
import static ru.zim.ates.common.messaging.config.MqConfig.ATES_USERS_EXCHANGE;
import static ru.zim.ates.common.messaging.config.MqConfig.ATES_USERS_STREAM_BILLING_QUEUE;
import static ru.zim.ates.common.messaging.config.MqConfig.ATES_USERS_STREAM_EXCHANGE;
import static ru.zim.ates.common.messaging.config.MqConfig.ATES_USERS_STREAM_TASK_TRACKER_QUEUE;
import static ru.zim.ates.common.messaging.config.MqConfig.ATES_USERS_TASK_TRACKER_QUEUE;

@Configuration
public class AppRabbitMqConfig {

    private RabbitAdmin admin;

    @Autowired
    private TasksPricesTaskTrackerQueue tasksPricesTaskTrackerQueue;
    @Autowired
    private TasksBillingQueue tasksBillingQueue;
    @Autowired
    private TasksStreamBillingQueue tasksStreamBillingQueue;

    @Autowired
    public AppRabbitMqConfig (ConnectionFactory connectionFactory) {
        admin = new RabbitAdmin(connectionFactory);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void cleanQueues() {
        //Чтобы было проще выполнять отладку мы можем чистить некоторые очереди на старте
        admin.purgeQueue(tasksPricesTaskTrackerQueue.getName(), false);

        admin.purgeQueue(tasksBillingQueue.getName(), false);
        admin.purgeQueue(tasksStreamBillingQueue.getName(), false);

    }


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
    @Bean
    TasksTopicExcange tasksTopicExchange() {
        return new TasksTopicExcange(ATES_TASKS_EXCHANGE, false, false);
    }
    @Bean
    TasksPricesTopicExcange tasksPricesTopicExchange() {
        return new TasksPricesTopicExcange(ATES_TASKS_PRICES_EXCHANGE, false, false);
    }
    @Bean
    TasksStreamTopicExcange tasksStreamTopicExcange() {
        return new TasksStreamTopicExcange(ATES_TASKS_STREAM_EXCHANGE, false, false);
    }

    //TasksStreamTopicExcange

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
    @Bean
    public TasksBillingQueue tasksBillingQueue() {
        return new TasksBillingQueue(ATES_TASKS_BILLING_QUEUE, false, false, false);
    }
    @Bean
    public TasksStreamBillingQueue tasksStreamBillingQueue() {
        return new TasksStreamBillingQueue(ATES_TASKS_STREAM_BILLING_QUEUE, false, false, false);
    }
    @Bean
    public TasksPricesTaskTrackerQueue tasksPricesTaskTrackerQueue() {
        return new TasksPricesTaskTrackerQueue(ATES_TASKS_PRICES_TASK_TRACKER_QUEUE, false, false, false);
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
    @Bean
    Binding tasksBillingBinding(TasksBillingQueue queue, TasksTopicExcange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with("");
    }
    @Bean
    Binding tasksStreamBillingBinding(TasksStreamBillingQueue queue, TasksStreamTopicExcange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with("");
    }
    @Bean
    Binding tasksPricesTaskTrackerBinding(TasksPricesTaskTrackerQueue queue, TasksPricesTopicExcange exchange) {
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
    public static class TasksTopicExcange extends TopicExchange {
        public TasksTopicExcange(String name, boolean durable, boolean autoDelete) {
            super(name, durable, autoDelete);
        }

    }
    public static class TasksStreamTopicExcange extends TopicExchange {
        public TasksStreamTopicExcange(String name, boolean durable, boolean autoDelete) {
            super(name, durable, autoDelete);
        }

    }
    public static class TasksPricesTopicExcange extends TopicExchange {
        public TasksPricesTopicExcange(String name, boolean durable, boolean autoDelete) {
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
    public static class TasksBillingQueue extends Queue {
        public TasksBillingQueue(String name, boolean durable, boolean exclusive, boolean autoDelete) {
            super(name, durable, exclusive, autoDelete);
        }
    }
    public static class TasksStreamBillingQueue extends Queue {
        public TasksStreamBillingQueue(String name, boolean durable, boolean exclusive, boolean autoDelete) {
            super(name, durable, exclusive, autoDelete);
        }
    }
    public static class TasksPricesTaskTrackerQueue extends Queue {
        public TasksPricesTaskTrackerQueue(String name, boolean durable, boolean exclusive, boolean autoDelete) {
            super(name, durable, exclusive, autoDelete);
        }
    }


}
