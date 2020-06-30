package com.bidr.auth2;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
/**
 * @Author liuxiaobin
 * @Description 启动类
 * @Date  2020/6/19
 **/
@SpringBootApplication
@MapperScan(basePackages = "com.bidr.auth2.auth")
public class Oauth2CodeApplication {
    public static void main(String[] args) {
        SpringApplication.run(Oauth2CodeApplication.class,args);
    }
}
