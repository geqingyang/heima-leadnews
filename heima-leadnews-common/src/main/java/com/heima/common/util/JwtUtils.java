package com.heima.common.util;

import com.heima.common.dtos.Payload;
import io.jsonwebtoken.*;
import org.joda.time.DateTime;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.*;

/**
 * JWT 工具类
 * 对JJWT的封装
 */
public class JwtUtils {

    private static final String JWT_PAYLOAD_USER_KEY = "id";
    // 加密KEY
    private static final String TOKEN_ENCRY_KEY = "MDk4ZjZiY2Q0NjIxZDM3M2NhZGU0ZTgzMjYyN2I0ZjY";


    /**
     * 加密token
     *
     * @param userId 载荷中的数据
     * @param expire 过期时间，单位分钟
     * @return JWT
     */
    public static String generateTokenExpireInMinutes(Integer userId, int expire) {
        Map<String, Object> claimMaps = new HashMap<>();
        claimMaps.put(JWT_PAYLOAD_USER_KEY,userId);
        return Jwts.builder()
                .addClaims(claimMaps)
                .setId(createJTI())
                .setExpiration(DateTime.now().plusMinutes(expire).toDate())
                .compressWith(CompressionCodecs.GZIP)  //数据压缩方式
                .setIssuedAt(new Date(System.currentTimeMillis()))  //签发时间
                .setSubject("system")  //说明
                .setIssuer("heima") //签发者信息
                .setAudience("app")  //接收用户
                .signWith(SignatureAlgorithm.HS256, generalKey())
                .compact();
    }

    /**
     * 由字符串生成加密key
     *
     * @return
     */
    public static SecretKey generalKey() {
        byte[] encodedKey = Base64.getEncoder().encode(TOKEN_ENCRY_KEY.getBytes());
        SecretKey key = new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");
        return key;
    }

    /**
     * 加密token
     *
     * @param userId 载荷中的数据
     * @param expire 过期时间，单位秒
     * @return JWT
     */
    public static String generateTokenExpireInSeconds(Integer userId, int expire) {
        Map<String, Object> claimMaps = new HashMap<>();
        claimMaps.put(JWT_PAYLOAD_USER_KEY,userId);
        return Jwts.builder()
                .addClaims(claimMaps)
                .setId(createJTI())
                .setExpiration(DateTime.now().plusSeconds(expire).toDate())
                .compressWith(CompressionCodecs.GZIP)  //数据压缩方式
                .setIssuedAt(new Date(System.currentTimeMillis()))  //签发时间
                .setSubject("system")  //说明
                .setIssuer("heima") //签发者信息
                .setAudience("app")  //接收用户
                .signWith(SignatureAlgorithm.HS256, generalKey())
                .compact();
    }

    /**
     * 解析token
     *
     * @param token 用户请求中的token
     * @return Jws<Claims>
     */
    private static Jws<Claims> parserToken(String token) {
        return Jwts.parser().setSigningKey(generalKey()).parseClaimsJws(token);
    }

    private static String createJTI() {
        return new String(Base64.getEncoder().encode(UUID.randomUUID().toString().getBytes()));
    }

    /**
     * 获取token中的用户信息
     *
     * @param token 用户请求中的令牌
     * @return 用户信息
     */
    public static Payload getInfoFromToken(String token) {
        Jws<Claims> claimsJws = parserToken(token);
        Claims body = claimsJws.getBody();
        Payload claims = new Payload();
        claims.setId(body.getId());
        claims.setUserId(Integer.valueOf(body.get(JWT_PAYLOAD_USER_KEY).toString()));
        claims.setExpiration(body.getExpiration());
        return claims;
    }

}