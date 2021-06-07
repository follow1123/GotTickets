package com.yang.gotTickets.util;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ArrayUtil;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.Function;

/**
 * @auther YF
 * @create 2021-05-15-16:52
 */
@Component
public class ConvertUtil {


    public <T> T convert(Object val, Class<T> type, T defaultVal){
        if (val == null || type == null) return defaultVal;
        List<Class<?>> list = new ArrayList<>();
        List<Class<?>> list1 = new ArrayList<>();
        getAllSuper(val.getClass(), list);
        getAllInterface(val.getClass(), list1);
        if (list.contains(type) || list1.contains(type)){
            return (T) val;
        }
        return defaultVal;
    }

    public String toStr(Object val, String defaultValue){
        if (val == null || defaultValue == null) return defaultValue;

        if (val instanceof String){
            return (String) val;
        }else return defaultValue;
    }

    public String toStr(Object val){
        return toStr(val, null);
    }

    public <V> List<V> toList(Object val, Class<V> valueType, List<V> defaultValue){
        if (val == null || valueType == null) return null;
        if (val instanceof List){
            List list = (List) val;
            if (!list.isEmpty()) {
                Object o = list.get(0);
                if (convert(o, valueType) != null)
                    return list;
            }
        }
        return defaultValue;
    }

    public <V> List<V> toList(Object val, Class<V> valueType){
        return toList(val, valueType, Collections.EMPTY_LIST);
    }


    public <T> T convert(Object val, Class<T> type){
        return convert(val, type, null);
    }

    private void getAllSuper(Class<?> val, List<Class<?>> classList){
        if (val == null || classList == null) return;
        classList.add(val);
        getAllSuper(val.getSuperclass(), classList);
    }

    private void getAllInterface(Class<?> val, List<Class<?>> classList){
        if (val == null || classList == null) return;
        ArrayList<Class<?>> classes = ListUtil.toList(val.getInterfaces());
        classList.addAll(classes);
        for (Class<?> aClass : classes) {
            getAllInterface(aClass, classList);
        }

    }


}
