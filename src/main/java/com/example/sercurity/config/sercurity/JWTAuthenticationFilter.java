package com.example.sercurity.config.sercurity;

import com.example.sercurity.utils.JwtTokenUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Author: plani
 * 创建时间: 2019/8/19 17:08
 */
//并不一定 非要用这个BasicAuthenticationFilter 作为父类，可以根据Filter的顺序 自己选择
public class JWTAuthenticationFilter extends BasicAuthenticationFilter {
    private Logger logger = LoggerFactory.getLogger(JWTAuthenticationFilter.class);
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Value("${jwt.tokenHeader}")
    private String tokenHeader;
    @Value("${jwt.tokenHead}")
    private String tokenHead;

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        //获取 header
        String authHeader = request.getHeader(this.tokenHeader);
        if (authHeader != null && authHeader.startsWith(this.tokenHead)) {
            //获取 token
            String authToken = authHeader.substring(this.tokenHead.length());// The part after "Bearer "
            //提取出 名字
            String username = jwtTokenUtil.getUserNameFromToken(authToken);
            logger.info("token 提取的username   >>> ", username + "   >>>>");
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
                if (jwtTokenUtil.validateToken(authToken, userDetails.getUsername())) {
                    //三个参数的构造方法  就是setAuthenticated  认证过了 ，可以看源码
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    logger.info("authenticated user:{}", username);
                    //设置到 SecurityContext里面
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        }
        //否则的  直接走流程  调用下一个Filter
        chain.doFilter(request, response);
    }


}
