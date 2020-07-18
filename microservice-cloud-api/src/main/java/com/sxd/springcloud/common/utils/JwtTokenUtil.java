package com.sxd.springcloud.common.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;

/**

 * @description: jwt生成token
 */
public class JwtTokenUtil {

    private static final String SECRET = "jwtsecret";

    /**
     * 生成token
     * @param subject （主体信息）
     * @param expirationSeconds 过期时间（秒）
     * @param claims 自定义身份信息
     * @return
     */
    public static String generateToken(String subject, int expirationSeconds, Map<String,Object> claims) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setExpiration(new Date(System.currentTimeMillis() + expirationSeconds * 1000))
                .signWith(SignatureAlgorithm.HS512, SECRET) // 不使用公钥私钥
//                .signWith(SignatureAlgorithm.RS256, privateKey)
                .compact();
    }

    /**
     * @deprecation: 解析token,获得subject中的信息
     */
    public static String parseToken(String token) {
        String subject = null;
        try {
            subject = getTokenBody(token).getSubject();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return subject;
    }

    //获取token自定义属性
    public static Map<String,Object> getClaims(String token){
        Map<String,Object> claims = null;
        try {
            claims = getTokenBody(token);
        }catch (Exception e) {
        }

        return claims;
    }

    // 是否已过期
    public static boolean isExpiration(String expirationTime){
        /*return getTokenBody(token).getExpiration().before(new Date());*/

        //通过redis中的失效时间进行判断
        String currentTime = DateUtil.getTime();
        if(DateUtil.compareDate(currentTime,expirationTime)){
            //当前时间比过期时间小，失效
            return true;
        }else{
            return false;
        }
    }

    private static Claims getTokenBody(String token){
        return Jwts.parser()
//                .setSigningKey(publicKey)
                .setSigningKey(SECRET) // 不使用公钥私钥
                .parseClaimsJws(token)
                .getBody();
    }
}
