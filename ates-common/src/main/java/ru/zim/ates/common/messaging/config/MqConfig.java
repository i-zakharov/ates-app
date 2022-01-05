package ru.zim.ates.common.messaging.config;

public interface MqConfig {

    String ATES_AUTH_PRODUCER_NAME = "ates-auth-app";
    String ATES_TASK_TRACKER_PRODUCER_NAME = "ates-task-tracker-app";
    String ATES_BILLING_PRODUCER_NAME = "ates-billing-app";

    String ATES_TEST_QUEUE = "ates-test-queue";
    String ATES_TEST_EXCHANGE = "ates-test-exchange";
    String ATES_TEST_ROOTING_KEY = "ates-test-routing_key";

    String ATES_USERS_TASK_TRACKER_QUEUE = "ates-users-task-tracker-queue";
    String ATES_USERS_BILLING_QUEUE = "ates-users-billing-queue";
    String ATES_USERS_EXCHANGE = "ates-users-exchange";

    String ATES_USERS_STREAM_TASK_TRACKER_QUEUE = "ates-users-stream-task-tracker-queue";
    String ATES_USERS_STREAM_BILLING_QUEUE = "ates-users-stream-billing-queue";
    String ATES_USERS_STREAM_EXCHANGE = "ates-users-stream-exchange";

    String ATES_TASKS_BILLING_QUEUE = "ates-tasks-billing-queue";
    String ATES_TASKS_EXCHANGE = "ates-tasks-exchange";

    String ATES_TASKS_PRICES_TASK_TRACKER_QUEUE = "ates-tasks-prices-task-tracker-queue";
    String ATES_TASKS_PRICES_EXCHANGE = "ates-tasks-prices-exchange";

    String ATES_TASKS_STREAM_EXCHANGE = "ates-tasks-stream-exchange";
    String ATES_TASKS_STREAM_BILLING_QUEUE = "ates-tasks-stream-billing-queue";

}
