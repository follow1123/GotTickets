package com.yang.gotTickets.util;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

/**
 * @auther YF
 * @create 2021-05-06-12:29
 */
@Component
@Scope(SCOPE_PROTOTYPE)
@Setter(onMethod_ = {@Autowired})
public class AuthUtil {

    private CusEncoder zipUtil;

    @Setter
    public BigInteger curCode = BigInteger.valueOf(16);

    public String getNextCode(){
        BigInteger value = curCode;
        curCode = curCode.shiftLeft(1);
        return zipUtil.zip(value);
    }

    public String getFirstCode(){
        return getNextCode(8);
    }
    public String getNextCode(Object seed){
        BigInteger integer = zipUtil.toBigInteger(seed);
        curCode = integer.shiftLeft(1);
        return zipUtil.zip(curCode);
    }

    public Set<String> getAuths(String authCode){
        if (StrUtil.isBlank(authCode)) return new HashSet<>();
        HashSet<String> authStr = new HashSet<>();
        getAuths(zipUtil.unzip(authCode), authStr);
        return authStr;
    }

    public Set<GrantedAuthority> getAuths(String authCode, Function<String, GrantedAuthority> ope){
        Set<String> auths = getAuths(authCode);
        return auths.stream().map(ope).collect(Collectors.toSet());
    }

    public void getAuths(BigInteger authNumber, Set<String> roles){
        if (authNumber.equals(BigInteger.ZERO)) return;
        BigInteger i =  highestOneBit(authNumber);
        getAuths(authNumber.subtract(i), roles);
        roles.add(zipUtil.zip(i));
    }

    /**
     * 类似Integer、Long的静态方法highestOneBit
     * 获取i下面最大的2的倍数
     * @param i
     * @return
     */
    private BigInteger highestOneBit(BigInteger i) {
        List<Long> list = getList(Long.highestOneBit(i.toString(2).length()) << 1);
        for (Long l : list) {
            i = i.or(i.shiftRight(Convert.toInt(l)));
        }
        return i.subtract(i.shiftRight(1));
    }

    /**
     * 获取类似 Integer、Long的静态方法highestOneBit里面需要位移的2的倍数
     * @param max
     * @return
     */
    private List<Long> getList(long max){
        ArrayList<Long> longs = new ArrayList<>();
        do {
            longs.add(max);
        }while ((max = (max >> 1)) > 0);
        Collections.reverse(longs);
        return longs;
    }

}
