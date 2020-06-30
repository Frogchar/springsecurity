package com.bidr.basic;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.bidr.basic.mapper")
public class BasicSecurityApplication {
    public static void main(String[] args) {
        SpringApplication.run(BasicSecurityApplication.class, args);
    }

}
