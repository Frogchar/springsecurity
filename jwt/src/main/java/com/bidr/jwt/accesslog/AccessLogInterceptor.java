package com.bidr.jwt.accesslog;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
/**
 * @Author liuxiaobin
 * @Description 日志拦截器
 * @Date  2020/6/18
 * @Param
 * @return
 **/
@Slf4j
public class AccessLogInterceptor implements HandlerInterceptor {
    //请求开始时间标识
    private static final String LOGGER_SEND_TIME = "SEND_TIME";
    //请求日志实体标识
    private static final String LOGGER_ACCESSLOG = "ACCESSLOG_ENTITY";

    /**
     * @Author liuxiaobin
     * @Description 进入SpringMVC的Controller之前开始记录日志实体
     * @Date  2020/6/18
     * @Param [request, response, o]
     * @return boolean
     **/
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {

        //创建日志实体
        AccessLog accessLog = new AccessLog();

        // 设置IP地址
        accessLog.setIp(AdrressIpUtils.getIpAdrress(request));

        //设置请求方法,GET,POST...
        accessLog.setHttpMethod(request.getMethod());

        //设置请求路径
        accessLog.setUrl(request.getRequestURI());

        //设置请求开始时间
        request.setAttribute(LOGGER_SEND_TIME,System.currentTimeMillis());

        //设置请求实体到request内，方便afterCompletion方法调用
        request.setAttribute(LOGGER_ACCESSLOG,accessLog);
        return true;
    }

    /**
     * @Author liuxiaobin
     * @Description 完成处理类
     * @Date  2020/6/18
     * @Param [request, response, o, e]
     * @return void
     **/
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object o, Exception e) throws Exception {

        //获取本次请求日志实体
        AccessLog accessLog = (AccessLog) request.getAttribute(LOGGER_ACCESSLOG);

        //获取请求错误码，根据需求存入数据库，这里不保存
        int status = response.getStatus();
        accessLog.setHttpStatus(status);

        //当前时间
        long currentTime = System.currentTimeMillis();

        //请求开始时间
        long snedTime = Long.valueOf(request.getAttribute(LOGGER_SEND_TIME).toString());


        //设置请求时间差
        accessLog.setDuration(Integer.valueOf((currentTime - snedTime)+""));

        accessLog.setCreateTime(new Date());
        //将sysLog对象持久化保存
        log.info(accessLog.toString());
    }
}

