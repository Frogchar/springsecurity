package com.bidr.basic.config;

import com.bidr.basic.handler.MyAuthenticationFailureHandler;
import com.bidr.basic.handler.MyAuthenticationSuccessHandler;
import com.bidr.basic.strategy.CustomExpiredSessionStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.annotation.Resource;
/**
 * always：如果当前请求没有session存在，Spring Security创建一个session。
 * ifRequired（默认）： Spring Security在需要时才创建session
 * never： Spring Security将永远不会主动创建session，但是如果session已经存在，它将使用该session
 * stateless：Spring Security不会创建或使用任何session。适合于接口型的无状态应用，该方式节省资源。
 * maximumSessions表示同一个用户最大的登录数量
 * maxSessionsPreventsLogin提供两种session保护策略：true表示已经登录就不予许再次登录，false表示允许再次登录但是之前的登录会下线。
 *expiredSessionStrategy表示自定义一个session被下线(超时)之后的处理策略。
 *
 **/
//@Configuration
//@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Resource
    private MyAuthenticationSuccessHandler myAuthenticationSuccessHandler;
    @Resource
    private MyAuthenticationFailureHandler myAuthenticationFailureHandler;
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //基于HttpBasic方式的认证
       /* http.httpBasic()//开启httpbasic认证
                .and()
                .authorizeRequests()
                .anyRequest()
                .authenticated();//所有请求都需要登录认证才能访问*/

       //基于Form表单的认证
        http.csrf().disable() //禁用跨站csrf攻击防御，后面的章节会专门讲解
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
                .antMatchers("/login.html","/login","/invalidSession.html").permitAll()//不需要通过登录验证就可以被访问的资源路径
                .antMatchers("/biz1","/biz2") //需要对外暴露的资源路径
                .hasAnyAuthority("ROLE_user","ROLE_admin")  //user角色和admin角色都可以访问
                .antMatchers("/syslog","/sysuser")
                .hasAnyRole("admin")  //admin角色可以访问
                //.antMatchers("/syslog").hasAuthority("sys:log")
                //.antMatchers("/sysuser").hasAuthority("sys:user")
                .anyRequest().authenticated()
                //设置session
              .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)//设置session创建策略
                .invalidSessionUrl("/invalidSession.html")//非法超时session跳转页面
                .sessionFixation().newSession()
                .maximumSessions(1)
                .maxSessionsPreventsLogin(true)
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
        auth.inMemoryAuthentication()
                .withUser("user")
                .password(passwordEncoder().encode("123456"))
                .roles("user")
                .and()
                .withUser("admin")
                .password(passwordEncoder().encode("123456"))
                //.authorities("sys:log","sys:user")
                .roles("admin")
                .and()
                .passwordEncoder(passwordEncoder());//配置BCrypt加密
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
