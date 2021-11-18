package ru.zim.ates.common.schemaregistry;

public interface MqConfig {

    public static final String ATES_AUTH_PRODUCER_NAME = "ates-auth-app";
    public static final String ATES_TASK_TRACKER_PRODUCER_NAME = "ates-task-tracker-app";
    public static final String ATES_BILLING_PRODUCER_NAME = "ates-billing-app";

    public static final String ATES_TEST_QUEUE = "ates-test-queue";
    public static final String ATES_TEST_EXCHANGE = "ates-test-exchange";
    public static final String  ATES_TEST_ROOTING_KEY = "ates-test-routing_key";

    public static final String ATES_USERS_TASK_TRACKER_QUEUE = "ates-users-task-tracker-queue";
    public static final String ATES_USERS_BILLING_QUEUE = "ates-users-billing-queue";
    public static final String ATES_USERS_EXCHANGE = "ates-users-exchange";

    public static final String ATES_USERS_STREAM_TASK_TRACKER_QUEUE = "ates-users-stream-task-tracker-queue";
    public static final String ATES_USERS_STREAM_BILLING_QUEUE = "ates-users-stream-billing-queue";
    public static final String ATES_USERS_STREAM_EXCHANGE = "ates-users-stream-exchange";

}
