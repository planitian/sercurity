package com.example.sercurity;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;

import java.util.Collections;
import java.util.HashMap;

//
//@RunWith(SpringRunner.class)
//@SpringBootTest
public class SercurityApplicationTests {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Test
    public void contextLoads() throws JsonProcessingException {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        System.out.println(bCryptPasswordEncoder.encode("password"));
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken("ss","ss");
        OAuth2Request oAuth2Request = new OAuth2Request(new HashMap<>(),"shishi", Collections.EMPTY_LIST,true,Collections.EMPTY_SET,Collections.EMPTY_SET,"s",Collections.EMPTY_SET
        ,new HashMap<>());
        OAuth2Authentication auth2Authentication = new OAuth2Authentication(oAuth2Request, usernamePasswordAuthenticationToken);
        JdkSerializationRedisSerializer OBJECT_SERIALIZER = new JdkSerializationRedisSerializer();
        byte[] serialize = OBJECT_SERIALIZER.serialize(auth2Authentication);
        System.out.println(serialize);


        ArrayNode arrayNode = new ArrayNode(JsonNodeFactory.instance);
        ObjectNode objectNode = new ObjectNode(JsonNodeFactory.instance);
        objectNode.put("name", "庄飞虎");
        arrayNode.add(objectNode);
        ObjectMapper objectMapper = new JsonMapper();
        String s = objectMapper.writeValueAsString(arrayNode);

        System.out.println(s);
    }

}
