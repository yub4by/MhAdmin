package com.yppah.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Date;

@Data
@Component
@ConfigurationProperties(prefix = "yppah.jwt")
public class JwtUtil {

    // 值在application.yml中设置
    private long expire; // 过期时长
    private String secret; // 秘钥
    private String header; // 自定义jwt的统一名称

    // 生成jwt
    public String generateToken(String username){
        Date nowDate = new Date();
        Date expireDate = new Date(nowDate.getTime() + 1000*expire); //过期时间
        return Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setSubject(username)
                .setIssuedAt(nowDate)
                .setExpiration(expireDate) //设置7天过期
                .signWith(SignatureAlgorithm.HS512, secret) //采用HS512算法进行加密
                .compact();
    }

    // 解析jwt
    public Claims getClaimByToken(String jwt){
        try {
            return Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(jwt)
                    .getBody();
        }catch (Exception e){
            return null;
        }
    }

    // 判断jwt是否过期
    public boolean isTokenExpired(Claims claims){
        return claims.getExpiration().before(new Date());
    }

}
