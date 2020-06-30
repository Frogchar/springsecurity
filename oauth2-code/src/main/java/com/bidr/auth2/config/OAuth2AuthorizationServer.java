package com.bidr.auth2.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;

import javax.annotation.Resource;
/**
 * @Author liuxiaobin
 * @Description
 * 授权码模式
 * 1、使用该连接访问浏览器
 * http://localhost:9001/oauth/authorize?client_id=client1&redirect_uri=http://localhost:8888/callback&response_type=code&scope=all
 *      /oauth/authorize为获取授权码的地址，由Spring Security OAuth项目提供
 *      client_id即我们认证服务器中配置的client
 *      redirect_uri即回调地址，授权码的发送地址该地址为第三方客户端应用的地址。要和我们之前配置的回调地址对上。
 *      response_type=code表示希望获取的响应内容为授权码
 *      scope表示申请的权限范围
 * 2、出现登录页面用户输入用户名和密码
 * 3、如果我们勾选Approve（同意），即可完成认证，向第三方客户端应用发放授权码。
 * 4、获取授权码
 *    使用postman或者curl 命令测试获取token
 *    curl -X POST --user client1:123456 http://localhost:9001/oauth/token  -H "content-type: application/x-www-form-urlencoded" -d "code=2gMHpI&grant_type=authorization_code&redirect_uri=http://localhost:8888/callback&scope=all"
 * @Date  2020/6/19
 * @Param
 * @return
 **/
@Configuration
@EnableAuthorizationServer
public class OAuth2AuthorizationServer extends AuthorizationServerConfigurerAdapter {

    @Resource
    PasswordEncoder passwordEncoder;
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
                .withClient("client1").secret(passwordEncoder.encode("123456")) // Client 账号、密码。
                .redirectUris("http://localhost:8888/callback") // 配置回调地址，选填。
                .authorizedGrantTypes("authorization_code") // 授权码模式
                .scopes("all"); // 可授权的 Scope
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
        oauthServer
                .tokenKeyAccess("permitAll()")
                .checkTokenAccess("permitAll()")
                .allowFormAuthenticationForClients();
    }


}