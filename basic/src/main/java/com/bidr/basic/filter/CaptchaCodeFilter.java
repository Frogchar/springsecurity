package com.bidr.basic.filter;

import cn.hutool.core.util.StrUtil;
import com.bidr.basic.common.CaptchaCode;
import com.bidr.basic.common.MyContants;
import com.bidr.basic.handler.MyAuthenticationFailureHandler;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.filter.OncePerRequestFilter;
import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Objects;
/**
 * @Author liuxiaobin
 * @Description
 * 验证码拦截器 继承自OncePerRequestFilter
 * 读取源码流程
 * 1、CaptchaCodeFilter过滤器中从seesion获取验证码文字与用户输入比对，比对通过执行其他过滤器链
 * 2、比对不通过，抛出SessionAuthenticationException异常，交给AuthenticationFailureHandler处理
 * 3、最后将CaptchaCodeFilter放在UsernamePasswordAuthenticationFilter表单过滤器之前执行。
 * @Date  2020/6/18
 * @Param
 * @return
 **/
@Component
public class CaptchaCodeFilter extends OncePerRequestFilter {

    @Resource
    MyAuthenticationFailureHandler myAuthenticationFailureHandler;
    /**
     * @Author liuxiaobin
     * @Description //拦截配置
     * @Date  2020/6/18
     * @Param [request, response, filterChain]
     * @return void
     **/
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        if(StrUtil.equals("/login",request.getRequestURI())
                && StrUtil.equalsIgnoreCase(request.getMethod(),"post")){
            try{
                //验证码与用户输入是否匹配
                validate(new ServletWebRequest(request));
            }catch(AuthenticationException e){
                myAuthenticationFailureHandler.onAuthenticationFailure(
                        request,response,e
                );
                return;
            }

        }

        filterChain.doFilter(request,response);

    }
    /**
     * @Author liuxiaobin
     * @Description //校验验证码
     * @Date  2020/6/18
     * @Param [request]
     * @return void
     **/
    private void validate(ServletWebRequest request) throws ServletRequestBindingException {
        HttpSession session = request.getRequest().getSession();
        String codeInRequest = ServletRequestUtils.getStringParameter(
                request.getRequest(),"captchaCode");
        if(StringUtils.isEmpty(codeInRequest)){
            throw new SessionAuthenticationException("验证码不能为空");
        }
        // 3. 获取session池中的验证码谜底
        CaptchaCode codeInSession = (CaptchaCode)
                session.getAttribute(MyContants.CAPTCHA_SESSION_KEY);
        if(Objects.isNull(codeInSession)) {
            throw new SessionAuthenticationException("验证码不存在");
        }
        // 4. 校验服务器session池中的验证码是否过期
        if(codeInSession.isExpired()) {
            session.removeAttribute(MyContants.CAPTCHA_SESSION_KEY);
            throw new SessionAuthenticationException("验证码已经过期");
        }
        // 5. 请求验证码校验
        if(!StrUtil.equals(codeInSession.getCode(), codeInRequest)) {
            throw new SessionAuthenticationException("验证码不匹配");
        }

    }


}
