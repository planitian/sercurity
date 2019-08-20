package com.example.sercurity.config;

import com.example.sercurity.bo.SecurityUser;
import com.example.sercurity.component.RestAuthenticationEntryPoint;
import com.example.sercurity.component.RestfulAccessDeniedHandler;
import com.example.sercurity.config.sercurity.JWTAuthenticationFilter;
import com.example.sercurity.config.sercurity.JWTLoginFilter;
import com.example.sercurity.config.sercurity.MyAuthticationProvider;
import com.example.sercurity.entity.User;
import com.example.sercurity.service.PermissonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @Author: plani
 * 创建时间: 2019/8/16 17:50
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true,securedEnabled = true,jsr250Enabled = true)
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {
    private Logger logger = LoggerFactory.getLogger(SpringSecurityConfig.class);
    @Autowired
    private PermissonService permissonService;

    @Autowired
    private RestfulAccessDeniedHandler restfulAccessDeniedHandler;
    @Autowired
    private RestAuthenticationEntryPoint restAuthenticationEntryPoint;

    @Autowired
    private MyAuthticationProvider myAuthticationProvider;

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
 /*       httpSecurity.csrf().disable()
                .authorizeRequests().anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login")//用户没有登录的话，登录页面
                .usernameParameter("userName")
                .passwordParameter("password")
                .failureUrl("/login?error")//这个是验证密码错误，以后要跳转的页面
                .permitAll()
                .and()
        .authorizeRequests();*/
//        httpSecurity.authorizeRequests().anyRequest().permitAll();
        httpSecurity.csrf().disable()
                .sessionManagement()// 基于token，所以不需要session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/login")//login接口不需要授权
                .permitAll()
                .anyRequest()
                .authenticated();
        httpSecurity.exceptionHandling().accessDeniedHandler(restfulAccessDeniedHandler).authenticationEntryPoint(restAuthenticationEntryPoint);
        httpSecurity.addFilter(new JWTLoginFilter(authenticationManager()))
                .addFilter(jwtAuthenticationFilter());
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth    // 设置UserDetailsService
                .userDetailsService(userDetailsService())
                // 使用BCrypt进行密码的hash
                .passwordEncoder(passwordEncoder());
//                .and()
//                .authenticationProvider(myAuthticationProvider);
    }


    // 装载BCrypt密码编码器
    @Bean
    public PasswordEncoder passwordEncoder() {//密码加密
        return new BCryptPasswordEncoder() {
            @Override
            public String encode(CharSequence rawPassword) {
                return rawPassword.toString();
            }

            @Override
            public boolean matches(CharSequence rawPassword, String encodedPassword) {
                return rawPassword.equals(encodedPassword);
            }
        };
    }


    @Override
    @Bean
    public UserDetailsService userDetailsService() {
        //获取登录用户信息
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                logger.info("username "+username);
                User user = permissonService.getUserByUserName(username);
                if (user != null) {
                    return new SecurityUser(user, permissonService);
                }
                throw new UsernameNotFoundException("没有用户");
            }
        };
    }


    @Bean
    public JWTAuthenticationFilter jwtAuthenticationFilter() {
        try {
            return new JWTAuthenticationFilter(authenticationManager());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
