package ru.zim.ates.auth.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static ru.zim.ates.common.config.MqConfig.*;

@Service
public class TestMessageProducerService {
    @Autowired
    public RabbitTemplate rabbitTemplate;

    public void sendMessage(String message) {
        rabbitTemplate.convertAndSend(ATES_EXCHANGE_TEST,
                ATES_ROOTING_KEY_TEST, message);
    }

}
