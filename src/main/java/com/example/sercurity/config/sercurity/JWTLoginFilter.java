package com.example.sercurity.config.sercurity;

import com.example.sercurity.bo.SecurityUser;
import com.example.sercurity.utils.JwtTokenUtil;
import com.example.sercurity.utils.SpringContextUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

/**
 * @Author: plani
 * 创建时间: 2019/8/19 9:49
 */
public class JWTLoginFilter extends UsernamePasswordAuthenticationFilter {
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        UsernamePasswordAuthenticationToken authRequest = null;
        if (MediaType.APPLICATION_JSON_VALUE.equals(request.getContentType()) || MediaType.APPLICATION_JSON_UTF8_VALUE.equals(request.getContentType())) {
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                JsonNode jsonNode = objectMapper.readTree(request.getInputStream());
                String username = jsonNode.get("username").asText("");
                String password = jsonNode.get("password").asText("");
                username = username.trim();
                //用于简单表示用户名和密码的身份验证}实现。
                authRequest = new UsernamePasswordAuthenticationToken(
                        username, password);
            } catch (IOException e) {
                logger.error(e.getMessage());
                authRequest = new UsernamePasswordAuthenticationToken("", "");
            }finally {
                //setDetails(request, authRequest) 是将当前的请求信息设置到 UsernamePasswordAuthenticationToken 中。
                setDetails(request, authRequest);
            }
            return this.getAuthenticationManager().authenticate(authRequest);
        } else {
            return super.attemptAuthentication(request, response);
        }
    }

    // 用户成功登录后，这个方法会被调用，我们在这个方法里生成token
    @Override
    protected void successfulAuthentication(HttpServletRequest req,
                                            HttpServletResponse res,
                                            FilterChain chain,
                                            Authentication auth) {
        String token = jwtTokenUtil.generateToken(((SecurityUser) auth.getPrincipal()).getUsername());
        res.addHeader("Authorization", "Bearer" + token);
    }
}
