package com.example.sercurity.config.sercurity;

import com.example.sercurity.bo.SecurityUser;
import com.example.sercurity.utils.JwtTokenUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Author: plani
 * 创建时间: 2019/8/19 9:49
 */
//UsernamePasswordAuthenticationFilter 默认拦截 /login 路径 获取参数
public class JWTLoginFilter extends UsernamePasswordAuthenticationFilter {
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    //在这里进行 进行身份认证。这个方法 会被父类调用
    //认证失败的 直接抛出异常就行 异常必须是AuthenticationException的本身或者 子类

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        UsernamePasswordAuthenticationToken authRequest = null;
        //处理 json数据
        if (MediaType.APPLICATION_JSON_VALUE.equals(request.getContentType()) || MediaType.APPLICATION_JSON_UTF8_VALUE.equals(request.getContentType())) {
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                //这里使用自己熟悉的json框架都行
                JsonNode jsonNode = objectMapper.readTree(request.getInputStream());
                String username = jsonNode.get("username").asText("");
                String password = jsonNode.get("password").asText("");
                username = username.trim();
                //用于简单表示用户名和密码的身份验证}实现。
                authRequest = new UsernamePasswordAuthenticationToken(
                        username, password);
            } catch (IOException e) {
                //抛出异常 ，必须是 AuthenticationException的子类
                throw new InternalAuthenticationServiceException(e.getMessage());
            }
            //setDetails(request, authRequest) 是将当前的请求信息设置到 UsernamePasswordAuthenticationToken 中。
            setDetails(request, authRequest);
            //调用 AuthenticationManager 进行 认证。这个就是我上面说的流程了
            //你也可以在这里 就进行 密码的验证，一切看自己 可定制度很高
            return this.getAuthenticationManager().authenticate(authRequest);
        } else {
            //其他的走，父类默认的方法
            return super.attemptAuthentication(request, response);
        }
    }

    //只要上面的 attemptAuthentication没有抛出异常 ，就一定会走这里
    // 用户成功登录后，这个方法会被调用，我们在这个方法里生成token
    @Override
    protected void successfulAuthentication(HttpServletRequest req,
                                            HttpServletResponse res,
                                            FilterChain chain,
                                            Authentication auth) throws IOException, ServletException {

        // 把完全填充的Authentication 设置 就是下面的这一行代码
        SecurityContextHolder.getContext().setAuthentication(auth);
        //把token返回  放在header中
        String token = jwtTokenUtil.generateToken(((SecurityUser) auth.getPrincipal()).getUsername());
        res.addHeader("Authorization", "Bearer" + token);
        res.getOutputStream().write("success  token".getBytes());
    }

    //这个是 登录失败，会被调用的 ，也就是上面attemptAuthentication 抛出异常 就会走这个方法
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        //这里可以自己 通过 response 写返回的信息，
        response.getOutputStream().write("error".getBytes());

        //也可以调用父类的 方法，走spring sercurity 定义好的流程 返回 ，配置文件里面定义的那些 failureHandler
        //默认实现的failureHandler 最后会指向/ 路径  ，这个要注意。
//        super.unsuccessfulAuthentication(request, response, failed);


    }
}
