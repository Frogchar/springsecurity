package com.bidr.jwt.service;

import com.bidr.jwt.mapper.MyRBACServiceMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Component("rabcService")
public class MyRBACService {

    @Resource
    private MyRBACServiceMapper myRBACServiceMapper;
    private AntPathMatcher antPathMatcher = new AntPathMatcher();
    /**
     * 判断某用户是否具有该request资源的访问权限
     */
    public boolean hasPermission(HttpServletRequest request, Authentication authentication){
        Object principal = authentication.getPrincipal();

        if(principal instanceof UserDetails){
            String username = ((UserDetails)principal).getUsername();

            List<String> urls = myRBACServiceMapper.findUrlsByUserName(username);

            return urls.stream().anyMatch(
                    url -> antPathMatcher.match(url,request.getRequestURI())
            );

        }
        return false;
    }
}

