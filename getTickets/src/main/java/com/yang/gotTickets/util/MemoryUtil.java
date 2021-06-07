package com.yang.gotTickets.util;

import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;

/**
 * @auther YF
 * @create 2021-05-15-15:04
 */
@Component
public class MemoryUtil {

    public void clear(Object... objects){
        for (Object object : objects) {
            if (object instanceof Collection){
                ((Collection) object).clear();
            }else if (object instanceof Map){
                ((Map) object).clear();
            }else throw new UnsupportedOperationException("未定义类型");
        }
    }

}
