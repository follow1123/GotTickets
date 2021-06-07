package com.yang.gotTickets.aspect.filter;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.StrUtil;
import com.yang.gotTickets.annotation.cache.Cached;
import com.yang.gotTickets.exception.BusException;
import com.yang.gotTickets.util.ExceptionUtil;
import com.yang.gotTickets.util.redisUtil.RedisData;
import com.yang.gotTickets.util.redisUtil.RedisFile;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.ReactiveSetCommands;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static cn.hutool.core.util.StrUtil.join;

/**
 * @auther YF
 * @create 2021-05-27-11:17
 * 缓存方法操作
 */
@Slf4j
@Aspect
@Component
@Setter(onMethod_ = {@Autowired})
public class CacheFilter {

    // redis 数据操作
    private RedisData redisUtil;
    // redis 文件夹操作
    private RedisFile redisFile;

    private ExceptionUtil exceptionUtil;

    private final String cacheMethodDir = "CACHE_METHOD";

    // 缓存方法对应的打断方法信息
    private final Map<Method, String> cacheMethods = new HashMap<>();


    @Pointcut("@annotation(com.yang.gotTickets.annotation.cache.Cached)")
    public void cacheFilter() {
    }

    @Pointcut("@annotation(com.yang.gotTickets.annotation.cache.CacheInterrupter)")
    public void interruptFilter() {
    }


    @Around("cacheFilter()")
    @SneakyThrows
    public Object around(ProceedingJoinPoint joinPoint) {
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        Cached cached = method.getAnnotation(Cached.class);
        Method interruptMethod = getInterruptMethod(cached);
        String dirName = joinPoint.getTarget().getClass().getSimpleName();
        String cacheKey = method.getName();
        if (method.getParameters().length != 0) throw new BusException("缓存方法不允许有参数");
        Optional<?> value = redisUtil.getValue(redisFile.concatDirName(cacheMethodDir, dirName), cacheKey, method.getReturnType());
        cacheMethods.put(interruptMethod, dirName + "_" + cacheKey);
        if (value.isPresent()) {
            CacheFilter.log.info("获取缓存中的数据：" + value.get());
            return value.get();
        }
        Object proceed = joinPoint.proceed(joinPoint.getArgs());
        redisUtil.putValue(redisFile.concatDirName(cacheMethodDir, dirName) , cacheKey, proceed, cached.timeout());
        CacheFilter.log.info("获取正常方法调用后的数据");
        return proceed;
    }

    @Before("interruptFilter()")
    public void before(JoinPoint joinPoint) {
        Method interruptMethod = ((MethodSignature) joinPoint.getSignature()).getMethod();
        String cacheKey = cacheMethods.get(interruptMethod);
        if (cacheKey != null) {
            log.info("清空 " + cacheKey + " 缓存");
            String[] s = cacheKey.split("_");
            redisUtil.remove(redisFile.concatDirName(cacheMethodDir, s[0]) , s[1]);
        }
    }

    private Method getInterruptMethod(Cached cached) {
        Class<?> clazz = cached.interruptBy();
        String methodName = cached.methodName();
        if (clazz == Cached.class || StrUtil.isBlank(methodName)) throw new BusException("类对象和方法名必须同时存在");
        for (Method method : clazz.getDeclaredMethods()) {
            if (method.getName().equals(methodName)) return method;
        }
        throw new IllegalArgumentException(join("", clazz.getName(), "对象内没有", methodName,"方法"));
    }

}
