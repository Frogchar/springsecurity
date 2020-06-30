package com.bidr.jwt.handler;
import com.bidr.jwt.common.RestResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
/**
 * @Author liuxiaobin
 * @Description 认证成功处理类
 * @Date  2020/6/18
 * @Param
 * @return
 **/
@Component
public class MyLogoutSuccessHandler implements LogoutSuccessHandler {
    private static ObjectMapper objectMapper = new ObjectMapper();
    @Override
    public void onLogoutSuccess(HttpServletRequest request,
                                HttpServletResponse response,
                                Authentication authentication)
            throws IOException, ServletException {

        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(
                RestResult.success()
        ));
        response.sendRedirect("/login.html");

    }
}
