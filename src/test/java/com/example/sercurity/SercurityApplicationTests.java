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
//@RunWith(SpringRunner.class)
//@SpringBootTest
public class SercurityApplicationTests {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Test
    public void contextLoads() {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String first;
        System.out.println(first = bCryptPasswordEncoder.encode("123456"));
        System.out.println(bCryptPasswordEncoder.matches("123456", first));
        String two;
        System.out.println(two = bCryptPasswordEncoder.encode("123456"));
        System.out.println(bCryptPasswordEncoder.matches("123456", two));
        System.out.println(two.equals(first));
        System.out.println(BCrypt.hashpw("123456", two));

    }

}
