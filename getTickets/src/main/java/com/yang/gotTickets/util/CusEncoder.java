package com.yang.gotTickets.util;


import cn.hutool.core.util.StrUtil;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;

/**
 * @auther YF
 * @create 2021-05-16-17:44
 * 将十进制数变为自定义的79进制数
 * 减少十进制数显示的长度
 */
@Component
public class CusEncoder {

    // 基础数 79 满79进1
    private final BigInteger base = BigInteger.valueOf(79);

    /**
     * 压缩 将十进制数变为自定义的79进制数
     *
     * @param value
     * @return
     */
    public String zip(BigInteger value) {
        StringBuilder builder = new StringBuilder("");
        appendNumber(value, builder);
        return builder.toString();
    }


    /**
     * 将自定义的79进制数还原为10进制数
     *
     * @param value
     * @return
     */
    public BigInteger unzip(String value) {
        if (StrUtil.isBlank(value)) return BigInteger.ZERO;
        BigInteger val = BigInteger.ZERO;
        BigInteger temp = BigInteger.ONE;
        for (int i = value.length() - 1; i >= 0; i--, temp = temp.multiply(base)) {
            BigInteger cur = getCurValue(value.charAt(i));
            val = val.add(cur.multiply(temp));
        }
        return val;
    }

    public String add(Object o1, Object o2) {
        return add(toBigInteger(o1), toBigInteger(o2));
    }

    public String sub(Object o1, Object o2) {
        return sub(toBigInteger(o1), toBigInteger(o2));
    }

    public String add(BigInteger i1, BigInteger i2) {
        return zip(i1.add(i2));
    }

    public String sub(BigInteger i1, BigInteger i2) {
        return zip(i1.subtract(i2));
    }

    public BigInteger toBigInteger(Object o) {
        if (o == null) o = "";
        if (o instanceof Number) {
            return BigInteger.valueOf(Long.parseLong(o.toString()));
        } else if (o instanceof String) {
            return unzip((String) o);
        } else throw new UnsupportedOperationException("类型不支持");
    }

    /**
     * 将单独的79进制数还原威威10进行的显示方式
     *
     * @param c
     * @return
     */
    private BigInteger getCurValue(char c) {
        return c >= 48 && c <= 57 ? new BigInteger(Character.valueOf(c).toString()) :
                BigInteger.valueOf(c - 48);
    }

    /**
     * 将十进制数变为自定义的79进制数 核心方法
     *
     * @param value
     * @param builder
     */
    private void appendNumber(BigInteger value, StringBuilder builder) {
        BigInteger divide = value.divide(base);
        BigInteger remainder = value.remainder(base);
        if (!divide.equals(BigInteger.ZERO))
            appendNumber(divide, builder);
        builder.append(getNumberChar(remainder.longValue()));
    }

    /**
     * 将单独的10进制数显示为79进制格式
     *
     * @param val
     * @return
     */
    private String getNumberChar(long val) {
        if (val > 9) return Character.valueOf((char) (val + 48)).toString();
        else return String.valueOf(val);
    }


}
