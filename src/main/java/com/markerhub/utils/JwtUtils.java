package com.markerhub.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Date;

@Data
@Component
@ConfigurationProperties(prefix = "markerhub.jwt")  //从配置文件中获取数据
public class JwtUtils {

    private long expire;
    private String secret;
    private String header;

    // 生成jwt
    public String generateToken(String username) {

        Date nowDate = new Date();
        Date expireDate = new Date(nowDate.getTime() + 1000 * expire);

        return Jwts.builder()
                .setHeaderParam("typ", "JWT")   //set header　里面的参数代表ｊｗｔ
                .setSubject(username)   //主体是username
                .setIssuedAt(nowDate)   //创建时间
                .setExpiration(expireDate)//过期时间 7天过期
                .signWith(SignatureAlgorithm.HS512, secret)  //加标签 SignatureAlgorithm算法+密钥
                .compact();  //合成一个jwt
    }

    // 解析jwt
    public Claims getClaimByToken(String jwt) {
        try {
            return Jwts.parser()  //解析器
                    .setSigningKey(secret)  //set密钥
                    .parseClaimsJws(jwt)
                    .getBody();
        } catch (Exception e) {
            return null;
        }
    }

    // jwt是否过期
    public boolean isTokenExpired(Claims claims) {
        return claims.getExpiration().before(new Date());
    }

}
