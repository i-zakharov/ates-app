package ru.zim.ates.auth.controller;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.zim.ates.auth.service.TestMessageProducerService;


@Slf4j
@Controller
@RequestMapping("/auth/test-message")
public class TestMessageController {

    @Autowired
    private TestMessageProducerService testMessageService;

    @GetMapping("/send")
    public String send() {
        return "send-test-message";
    }

    @PostMapping("/send")
    public @ResponseBody
    String send(@ModelAttribute("message") MessageDto messageDto) {
        log.info(messageDto.toString());
        testMessageService.sendMessage(messageDto.getText());
        return "Ok";
    }

    @Data
    public static class MessageDto {
        String text;
    }

}
