package com.yang.gotTickets.filter.authFilter.base;

import org.springframework.beans.factory.support.BeanDefinitionOverrideException;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

/**
 * @auther YF
 * @create 2021-05-25-15:25
 */
public class AbsRoleFilter implements RoleFilter{


    private final Map<Method, BiFunction<Object, Object[],Object[]>> preMap = new HashMap<>();

    private final Map<Method, BiFunction<Object, Object,Object>> sufMap = new HashMap<>();

    public Object getMark() {
        throw new RuntimeException("未实现获取标记方法");
    }

    @Override
    public Object[] preFilter(Method method, Object[] args) {
        BiFunction<Object, Object[], Object[]> function = preMap.get(method);
        if (function != null)
            return function.apply(getMark(), args);
        return args;
    }

    @Override
    public Object sufFilter(Method method, Object result) {
        BiFunction<Object, Object, Object> function = sufMap.get(method);
        if (function != null)
            return sufMap.get(method).apply(getMark(), result);
        return result;
    }

    public void putPreMap(Method method, BiFunction<Object, Object[],Object[]> function){
        preMap.put(method, function);
    }

    public void putSufMap(Method method, BiFunction<Object, Object,Object> function){
        sufMap.put(method, function);
    }
}
