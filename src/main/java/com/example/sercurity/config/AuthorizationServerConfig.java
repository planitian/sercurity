package com.example.sercurity.config;

import com.example.sercurity.config.sercurity.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;


/**
 * 配置认证服务器
 * 用户负责使用正常的Spring Security功能（@EnableWebSecurity等）保护授权端点（/ oauth / authorize）
 * ，但是令牌端点（/ oauth / token）将使用客户端凭据上的HTTP Basic身份验证自动得到保护。
 * 必须通过通过一个或多个AuthorizationServerConfigurers提供ClientDetailsService来注册客户端。
 */
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    /**
     * 这个需要用@Bean 来生成  ，spring 这个没有
     */
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private RedisConnectionFactory redisConnectionFactory;

    /**
     * 用户名 密码权限 等
     */
    @Autowired
    private MyUserDetailsService myUserDetailsService;

    @Bean
    public TokenStore tokenStore() {
        //tokenStore用redis的
        RedisTokenStore redisTokenStore = new RedisTokenStore(redisConnectionFactory);
        return redisTokenStore;
    }


    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    //配置客户端详细信息
    //配置客户端 信息 ，这里采用写在内存里面的方法 就是代码直接写入 ，不用数据库了，因为 没几个应用
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()//内存方式
                .withClient("shishi")//客户端id
                .scopes("read")//客户端范围
                .secret("$2a$10$aANgbMdbdMgdYiEPJ5l.p.3bDjTmnsoQuuct.eR/iErwpTZT4Nqxa")//客户端密码 这里需要加密后的
                /*
                * redirectUri 在进行authorization_code 认证时候，第一次get拿到code，第二次 请求/oauth/token 接口时候的 redirectUri要一致一样
                * redirectUri 并且要是这里面配置的
                * */
                .redirectUris("http://localhost:8881/shishi/re/sss","http://localhost:8881/shishi/re/callback")
                /*客户端对应的授权方式  授权码   一共五种 */
                .authorizedGrantTypes("authorization_code", "password", "refresh_token","client_credentials","implicit");
    }

    @Override
    //用来配置令牌端点(Token Endpoint)的安全约束
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        super.configure(security);
        security.allowFormAuthenticationForClients()
                .tokenKeyAccess("permitAll()")// 开启/oauth/token_key验证端口无权限访问
                .checkTokenAccess("isAuthenticated()")// 开启/oauth/check_token验证端口认证权限访问
                .passwordEncoder(passwordEncoder);//配置密码加解密的


    }

    @Override
    //管理令牌
    //用来配置授权（authorization）以及令牌（token）的访问端点和令牌服务(token services)。
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        super.configure(endpoints);
        endpoints
                .tokenStore(tokenStore())//设置tokenStore
                .tokenServices(defaultTokenServices())//设置tokenServices
                .userDetailsService(myUserDetailsService)//设置userDetailsService  这个就是详细的 从数据库里面 取出用户的密码，权限等 ，主要就是返回 UserDetails
                .authenticationManager(authenticationManager);//认证管理器
    }

    @Primary//强制 使用自定义的这个
    @Bean
    public DefaultTokenServices defaultTokenServices() {
        DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
        defaultTokenServices.setTokenStore(tokenStore()); //设置tokenStore
        defaultTokenServices.setSupportRefreshToken(true);//设置是否支持 refresh token
        //设置token 有效期  秒为单位
        defaultTokenServices.setAccessTokenValiditySeconds(60 * 60 * 2);
        //设置refresh token 有效期  秒为单位
        defaultTokenServices.setRefreshTokenValiditySeconds(60 * 60 * 8);
        return defaultTokenServices;
    }

}
