package ru.zim.ates.tasktracker;

import java.net.URI;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Slf4j
public class PasswordEncodingTest {
    @Test
    void encodePass() {
        String pass = "qwerty";
        String encodedPass = new BCryptPasswordEncoder().encode(pass);
        System.out.println("encodedPass = " + encodedPass);
    }



}
