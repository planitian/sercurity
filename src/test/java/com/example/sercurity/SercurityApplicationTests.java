package com.example.sercurity;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
//
@RunWith(SpringRunner.class)
@SpringBootTest
public class SercurityApplicationTests {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Test
    public void contextLoads() {
        String password = passwordEncoder.encode("123456");
        System.out.println(password);
    }

}
