package com.bidr.basic.config;

import com.bidr.basic.filter.CaptchaCodeFilter;
import com.bidr.basic.handler.MyAuthenticationFailureHandler;
import com.bidr.basic.handler.MyAuthenticationSuccessHandler;
import com.bidr.basic.handler.MyLogoutSuccessHandler;
import com.bidr.basic.service.impl.MyUserDetailsService;
import com.bidr.basic.strategy.CustomExpiredSessionStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.annotation.Resource;
import javax.sql.DataSource;

/**
 * 数据库访问
 * 动态实现权限加载
 * 首先将静态规则去掉（注释掉的部分内容），这部分内容我们将替换为动态从数据库加载
 * 登录页面“login.html”和登录认证处理路径“/login”需完全对外开发，不需任何鉴权就可以访问
 * 首页"/index"必须authenticated，即：登陆之后才能访问。不做其他额外鉴权规则控制。
 * 最后，其他的资源的访问我们通过权限规则表达式实现，表达式规则中使用了rbacService，
 * 这个类我们自定义实现。该类服务hasPermission从内存(或数据库)动态加载资源匹配规则，进行资源访问鉴权。
 **/
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityDbConfig extends WebSecurityConfigurerAdapter {

    @Resource
    private MyAuthenticationSuccessHandler myAuthenticationSuccessHandler;
    @Resource
    private MyAuthenticationFailureHandler myAuthenticationFailureHandler;
    @Resource
    private MyUserDetailsService myUserDetailsService;
    @Resource
    private DataSource datasource;
    @Resource
    private MyLogoutSuccessHandler myLogoutSuccessHandler;
    @Resource
    private CaptchaCodeFilter captchaCodeFilter;
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
            .addFilterBefore(captchaCodeFilter, UsernamePasswordAuthenticationFilter.class)
            .rememberMe()
                .rememberMeParameter("remember-me-new")
                .rememberMeCookieName("remember-me-cookie")
                .tokenValiditySeconds(2 * 24 * 60 * 60)
                .tokenRepository(persistentTokenRepository())
             //form表单验证
            .and()
                .csrf().disable() //禁用跨站csrf攻击防御，后面的章节会专门讲解
                .formLogin()
                .successHandler(myAuthenticationSuccessHandler)
                .failureHandler(myAuthenticationFailureHandler)
                .loginPage("/login.html")//用户未登录时，访问任何资源都转跳到该路径，即登录页面
                .loginProcessingUrl("/login")//登录表单form中action的地址，也就是处理认证请求的路径
                .usernameParameter("username")///登录表单form中用户名输入框input的name名，不修改的话默认是username
                .passwordParameter("password")//form中密码输入框input的name名，不修改的话默认是password
               // .defaultSuccessUrl("/index")//登录认证成功后默认转跳的路径
            //拦截配置
            .and()
                .authorizeRequests()
                .antMatchers("/login.html","/login","/invalidSession.html","/kaptcha").permitAll()//不需要通过登录验证就可以被访问的资源路径
                .antMatchers("/index").authenticated()
                .anyRequest().access("@rabcService.hasPermission(request,authentication)")
            //登出配置
            .and()
                .logout()
                .logoutUrl("/signout")
                //.logoutSuccessUrl(``"/aftersignout.html"``)
                .deleteCookies("JSESSIONID")
                //自定义logoutSuccessHandler
                .logoutSuccessHandler(myLogoutSuccessHandler)
                //设置session
            .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)//设置session创建策略
                .invalidSessionUrl("/invalidSession")//非法超时session跳转页面
                .sessionFixation().newSession()
                .maximumSessions(1)
                .maxSessionsPreventsLogin(false)
                .expiredSessionStrategy(new CustomExpiredSessionStrategy());

    }
    /**
     * @Author liuxiaobin
     * @Description /
     * inMemoryAuthentication指的是在内存里面存储用户的身份认证和授权信息。
     * withUser("user")用户名是user
     * password(passwordEncoder().encode("123456"))密码是加密之后的123456
     * authorities("sys:log","sys:user")指的是admin用户拥有资源ID对应的资源访问的的权限："/syslog"和"/sysuser"
     * roles()方法用于指定用户的角色，一个用户可以有多个角色
     * @Date  2020/6/17
     * @Param [auth]
     * @return void
     **/
    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(myUserDetailsService).passwordEncoder(passwordEncoder());
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public PersistentTokenRepository persistentTokenRepository(){

        JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
        tokenRepository.setDataSource(datasource);

        return tokenRepository;
    }
}
