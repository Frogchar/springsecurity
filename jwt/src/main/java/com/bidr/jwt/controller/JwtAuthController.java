package com.bidr.jwt.controller;
import cn.hutool.core.util.StrUtil;
import com.bidr.jwt.common.RestResult;
import com.bidr.jwt.exception.CustomException;
import com.bidr.jwt.exception.CustomExceptionType;
import com.bidr.jwt.service.JwtAuthService;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;
/**
 * @Author liuxiaobin
 * @Description //鉴权控制类--考虑以后封装
 * @Date  2020/6/18
 * @Param
 * @return
 **/
@RestController
public class JwtAuthController {
    @Resource
    JwtAuthService jwtAuthService;
    /**
     * @Author liuxiaobin
     * @Description //用户名密码换取token
     * @Date  2020/6/18
     * @Param [map]
     * @return com.bidr.jwt.common.RestResult
     **/
    @RequestMapping(value = "/authentication")
    public RestResult login(@RequestBody Map<String,String> map){
        String username  = map.get("username");
        String password = map.get("password");

        if(StrUtil.isEmpty(username)
                || StrUtil.isEmpty(password)){
            return RestResult.error(
                    new CustomException(CustomExceptionType.USER_INPUT_ERROR,
                    "用户名或者密码不能为空"));
        }
        try {
            return RestResult.success(jwtAuthService.login(username, password));
        }catch (CustomException e){
            return RestResult.error(e);
        }
    }
    /**
     * @Author liuxiaobin
     * @Description //刷新token
     * @Date  2020/6/18
     * @Param [token]
     * @return com.bidr.jwt.common.RestResult
     **/
    @RequestMapping(value = "/refreshtoken")
    public  RestResult refresh(@RequestHeader("${jwt.header}") String token){
            return RestResult.success(jwtAuthService.refreshToken(token));
    }

}
