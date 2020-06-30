package com.bidr.auth2.config;

import com.bidr.auth2.auth.MyUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;

import javax.annotation.Resource;
/**
 * @Author liuxiaobin
 * @Description
 * 认证服务器-刷新token
 * 1、配置方式为authorizedGrantTypes加上refresh_token配置
 *   为OAuth2AuthorizationServer配置类加入UserDetailsService,刷新令牌的时候需要用户信息
 * 2、这样当我们通过授权码模式和密码模式请求AccessToken的时候，
 *    返回结果中将多出一个字段refresh_token。（客户端模式和简化模式是不支持refresh_token）
 *    {
 *     "access_token": "735f8033-b02a-445f-b071-64b7a29b948b",
 *     "token_type": "bearer",
 *     "refresh_token": "42a24849-d074-401b-9058-5b1ef1660046",
 *     "expires_in": 43199,
 *     "scope": "all"
 * }
 * @Date  2020/6/19
 * @Param
 * @return
 **/
@Configuration
@EnableAuthorizationServer
public class OAuth2AuthorizationServer extends AuthorizationServerConfigurerAdapter {

    @Resource
    PasswordEncoder passwordEncoder;
    @Resource
    MyUserDetailsService myUserDetailsService;
    @Resource
    private AuthenticationManager authenticationManager;
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
                .withClient("client1").secret(passwordEncoder.encode("123456")) // Client 账号、密码。
                .redirectUris("http://localhost:8888/callback") // 配置回调地址，选填。
                .authorizedGrantTypes("refresh_token","password") // 刷新token
                .scopes("all") // 可授权的 Scope
                .accessTokenValiditySeconds(3600)//设置accessToken的有效期
                .refreshTokenValiditySeconds(7200);//设置refreshToken的有效期
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
        oauthServer
                .tokenKeyAccess("permitAll()")
                .checkTokenAccess("permitAll()")
                .allowFormAuthenticationForClients();
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.authenticationManager(authenticationManager).userDetailsService(myUserDetailsService);
    }
}