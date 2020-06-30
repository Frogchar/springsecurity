package com.bidr.jwt.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
/**
 * @Author liuxiaobin
 * @Description //测试类
 * @Date  2020/6/18
 * @Param
 * @return
 **/
@Slf4j
@RestController
public class HelloController {

    @RequestMapping(value = "/hello",method = RequestMethod.POST)
    public String hello() {
        return "world";
    }

}
