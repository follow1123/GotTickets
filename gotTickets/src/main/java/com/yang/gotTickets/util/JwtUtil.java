package com.yang.gotTickets.util;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.yang.gotTickets.bean.TUser;
import com.yang.gotTickets.config.ProjectProperties;
import jdk.nashorn.internal.parser.Token;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.jws.Oneway;
import java.io.Serializable;
import java.security.SecureRandom;
import java.util.*;

/**
 * @auther YF
 * @create 2021-05-09-15:57
 */
@Component
@Setter(onMethod_ = {@Autowired})
public class JwtUtil {

    private ProjectProperties properties;

    private  final String SING = ArrayUtil.join(RandomUtil.randomEleSet(Arrays.asList("!", 1, "a", "#", "!", 9, "(", "2321", "\\", 99, "<", "<>", new Date(), "88", ".", "_"), 8).stream().toArray(), "");
//    private  final String SING = "AAAA";


    public  String createToken(Map<String, Object> payload) {
        return JWT.create()
                .withPayload(payload)
                .withExpiresAt(DateUtil.offsetHour(DateUtil.date(), properties.getTokenExpire()))
//                .withExpiresAt(DateUtil.offsetHour(DateUtil.date(), 7 * 24))
                .sign(Algorithm.HMAC256(SING));
    }

    public  String createToken(Object bean) {
        Map<String, Object> map = BeanUtil.beanToMap(bean);
        map.entrySet().removeIf(entry -> entry.getValue() == null);
        return createToken(map);
    }

    public  boolean isExpired(String token, Map<String, Object> info){
        return verifyAndGet(token, info) instanceof TokenExpiredException;
    }

    public  boolean isExpired(String token, Object info){
        return verifyAndGet(token, info) instanceof TokenExpiredException;
    }

    public  boolean isWrongToken(String token, Map<String, Object> info){
        return verifyAndGet(token, info) != null;
    }

    public  boolean isWrongToken(String token, Object info){
        return verifyAndGet(token, info) != null;
    }


    public  JWTVerificationException verifyAndGet(String token, Map<String, Object> info){
        try {
           DecodedJWT jwt = JWT.require(Algorithm.HMAC256(SING)).build().verify(token);
           if (info != null && jwt != null){
               Map<String, Claim> claims = jwt.getClaims();
               for (String s : claims.keySet()) {
                   info.put(s, claims.get(s).as(Object.class));
               }
           }
           return null;
        }catch (JWTVerificationException e){
            return e;
        }
    }

    public  JWTVerificationException verifyAndGet(String token, Object info){
        HashMap<String, Object> map = new HashMap<>();
        JWTVerificationException exception = verifyAndGet(token, map);
        BeanUtil.fillBeanWithMap(map, info, CopyOptions.create().ignoreError());
        return exception;
    }

}
