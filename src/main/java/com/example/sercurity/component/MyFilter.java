package com.example.sercurity.component;


import org.springframework.http.MediaType;

import javax.servlet.*;
import java.io.IOException;

/**
 * @Author: plani
 * 创建时间: 2019/8/21 14:06
 */

public class MyFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
      /*  if (request.getContentType().equals(MediaType.APPLICATION_JSON_VALUE)){
            chain.doFilter(request, response);
        }else {
            response.getWriter().write("not support");
        }*/
        chain.doFilter(request, response);
    }
}
