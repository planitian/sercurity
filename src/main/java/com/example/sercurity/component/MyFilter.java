package com.example.sercurity.component;


import org.springframework.http.MediaType;

import javax.servlet.*;
import java.io.IOException;

/**
 * @Author: plani
 * 创建时间: 2019/8/21 14:06
 */
//
public class MyFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        //在这里 写你的业务逻辑
        System.out.println("我是自定义的逻辑  ");
         chain.doFilter(request, response);
    }
}
