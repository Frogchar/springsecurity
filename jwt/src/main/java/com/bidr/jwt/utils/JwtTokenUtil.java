package com.bidr.jwt.utils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author liuxiaobin
 * @Description //Jwt工具类
 * @Date  2020/6/18
 * @Param
 * @return
 **/
@Data
@ConfigurationProperties(prefix = "jwt")
@Component
public class JwtTokenUtil {

    private String secret;
    private Long expiration;
    private String header;


    /**
     * @Author liuxiaobin
     * @Description //生成token令牌
     * @Date  2020/6/18
     * @Param [userDetails]
     * @return java.lang.String
     **/
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>(2);
        claims.put("sub", userDetails.getUsername());
        claims.put("created", new Date());

        return generateToken(claims);
    }

    /**
     * @Author liuxiaobin
     * @Description //从令牌中获取用户名
     * @Date  2020/6/18
     * @Param [token]
     * @return java.lang.String
     **/
    public String getUsernameFromToken(String token) {
        String username;
        try {
            Claims claims = getClaimsFromToken(token);
            username = claims.getSubject();
        } catch (Exception e) {
            username = null;
        }
        return username;
    }

    /**
     * @Author liuxiaobin
     * @Description //判断令牌是否过期
     * @Date  2020/6/18
     * @Param [token]
     * @return java.lang.Boolean
     **/
    public Boolean isTokenExpired(String token) {
        try {
            Claims claims = getClaimsFromToken(token);
            Date expiration = claims.getExpiration();
            return expiration.before(new Date());
        } catch (Exception e) {
            return false;
        }
    }
    /**
     * @Author liuxiaobin
     * @Description  刷新令牌
     * @Date  2020/6/18
     * @Param [token]
     * @return java.lang.String
     **/
    public String refreshToken(String token) {
        String refreshedToken;
        try {
            Claims claims = getClaimsFromToken(token);
            claims.put("created", new Date());
            refreshedToken = generateToken(claims);
        } catch (Exception e) {
            refreshedToken = null;
        }
        return refreshedToken;
    }

    /**
     * @Author liuxiaobin
     * @Description  刷新令牌
     * @Date  2020/6/18
     * @Param [token, userDetails]
     * @return java.lang.Boolean
     **/
    public Boolean validateToken(String token, UserDetails userDetails) {

        String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }


    /**
     * @Author liuxiaobin
     * @Description  刷新令牌
     * @Date  2020/6/18
     * @Param [claims]
     * @return java.lang.String
     **/
    private String generateToken(Map<String, Object> claims) {
        Date expirationDate = new Date(System.currentTimeMillis() + expiration);
        return Jwts.builder().setClaims(claims)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

   /**
    * @Author liuxiaobin
    * @Description 从令牌中获取数据声明
    * @Date  2020/6/18
    * @Param [token]
    * @return Claims
    **/
    private Claims getClaimsFromToken(String token) {
        Claims claims;
        try {
            claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
        } catch (Exception e) {
            claims = null;
        }
        return claims;
    }

}
