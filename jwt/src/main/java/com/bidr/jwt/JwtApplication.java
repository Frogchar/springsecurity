package com.bidr.jwt;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
/**
 * @Author liuxiaobin
 * @Description //启动类
 * @Date  2020/6/18
 * @Param
 * @return
 **/
@SpringBootApplication
@MapperScan("com.bidr.jwt.mapper")
public class JwtApplication {
    public static void main(String[] args){
        SpringApplication.run(JwtApplication.class,args);
    }
}
