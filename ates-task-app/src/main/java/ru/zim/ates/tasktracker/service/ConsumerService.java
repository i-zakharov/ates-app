package ru.zim.ates.tasktracker.service;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.CountDownLatch;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ConsumerService {

    public static String messageBodyToString(Message message) {
        return new String (message.getBody(), StandardCharsets.UTF_8);
    }

    private CountDownLatch latch = new CountDownLatch(1);

    public void receiveMessage(Message message) {
        log.info("Received {}", messageBodyToString(message));
        latch.countDown();
    }

    public CountDownLatch getLatch() {
        return latch;
    }

}
