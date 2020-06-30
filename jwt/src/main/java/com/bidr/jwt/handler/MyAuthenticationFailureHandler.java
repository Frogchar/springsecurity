package com.bidr.jwt.handler;
import com.bidr.jwt.common.RestResult;
import com.bidr.jwt.exception.CustomException;
import com.bidr.jwt.exception.CustomExceptionType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
/**
 * @Author liuxiaobin
 * @Description 认证失败处理类
 * @Date  2020/6/18
 * @Param
 * @return
 **/
@Component
public class MyAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Value("${spring.security.loginType}")
    private String loginType;

    private static ObjectMapper objectMapper = new ObjectMapper();

    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException exception)
            throws IOException, ServletException {

        String errorMsg = "登录出现错误!";
        if(exception instanceof BadCredentialsException){
            errorMsg="密码错误";
        }
        if (exception instanceof UsernameNotFoundException){
            errorMsg="用户不存在";
        }
        if (exception instanceof LockedException){
            errorMsg="账户被锁定";
        }
        if (exception instanceof AccountExpiredException){
            errorMsg="账户过期";
        }
        if(exception instanceof AccountStatusException){
            errorMsg="账户状态异常";
        }
        if(exception instanceof SessionAuthenticationException){
            errorMsg = exception.getMessage();
        }

        if(loginType.equalsIgnoreCase("JSON")){
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(objectMapper.writeValueAsString(
                    RestResult.error(new CustomException(
                            CustomExceptionType.USER_INPUT_ERROR,
                            errorMsg))
            ));
        }else{
            //跳转到登陆页面
            super.onAuthenticationFailure(request,response,exception);
        }

    }
}
