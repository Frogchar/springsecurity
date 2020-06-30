package com.bidr.jwt.service;
import com.bidr.jwt.exception.CustomException;
import com.bidr.jwt.exception.CustomExceptionType;
import com.bidr.jwt.utils.JwtTokenUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
/**
 * @Author liuxiaobin
 * @Description //鉴权service
 * @Date  2020/6/18
 * @Param
 * @return
 **/
@Service
public class JwtAuthService {

    @Resource
    AuthenticationManager authenticationManager;

    @Resource
    UserDetailsService userDetailsService;

    @Resource
    JwtTokenUtil jwtTokenUtil;

    /**
     * @Author liuxiaobin
     * @Description 登录认证换取JWT令牌
     * @Date  2020/6/18
     * @Param [username, password]
     * @return java.lang.String
     **/
    public String login(String username,String password) throws CustomException {
        try {
            UsernamePasswordAuthenticationToken upToken =
                    new UsernamePasswordAuthenticationToken(username, password);
            Authentication authentication = authenticationManager.authenticate(upToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }catch (AuthenticationException e){
            throw new CustomException(CustomExceptionType.USER_INPUT_ERROR
                            ,"用户名或者密码不正确");
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        return jwtTokenUtil.generateToken(userDetails);
    }

    /**
     * @Author liuxiaobin
     * @Description 刷新token
     * @Date  2020/6/18
     * @Param [oldToken]
     * @return java.lang.String
     **/
    public String refreshToken(String oldToken){
        if(!jwtTokenUtil.isTokenExpired(oldToken)){
            return jwtTokenUtil.refreshToken(oldToken);
        }
        return null;
    }



}
