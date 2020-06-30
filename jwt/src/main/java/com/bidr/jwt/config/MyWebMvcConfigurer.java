package com.bidr.jwt.config;

import com.bidr.jwt.accesslog.AccessLogInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
/**
 * @Author liuxiaobin
 * @Description //设置排除路径，spring boot 2.*，注意排除掉静态资源的路径，不然静态资源无法访问
 * @Date  2020/6/18
 * @Param 
 * @return 
 **/
@Configuration
public class MyWebMvcConfigurer implements WebMvcConfigurer {
    private final String[] excludePath = {"/static"};

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AccessLogInterceptor()).addPathPatterns("/**").excludePathPatterns(excludePath);
    }
}