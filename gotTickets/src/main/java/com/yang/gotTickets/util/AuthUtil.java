package com.yang.gotTickets.util;

import cn.hutool.core.convert.Convert;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * @auther YF
 * @create 2021-05-06-12:29
 */
public class AuthUtil {


    private BigInteger code = BigInteger.ONE;

    public String getNextCode(){
        BigInteger value = code;
        code = code.shiftLeft(1);
        return value.toString(16);
    }
    public String getNextCode(BigInteger seed){
        code = seed.shiftLeft(1);
        return code.toString(16);
    }


    public void getAuths(BigInteger authNumber, Set<GrantedAuthority> roles){
        if (authNumber.equals(BigInteger.ZERO)) return;
        BigInteger i =  highestOneBit(authNumber);
        getAuths(authNumber.subtract(i), roles);
        roles.add(new SimpleGrantedAuthority(i.toString(16)));
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
