package com.yang.gotTickets.util;

import cn.hutool.core.util.StrUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import java.lang.reflect.Constructor;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

/**
 * @auther YF
 * @create 2021-06-02-15:55
 */
@Slf4j
@Component
public class ExceptionUtil {

    public <R> Optional<R> catchAngLog(Supplier<R> supplier, BiConsumer<Logger, String> lineHandler) {
        R r = null;
        try {
            r = supplier.get();
        } catch (Throwable e) {
            lineHandler.accept(log, e.getMessage());
        }
        if (r == null) return Optional.empty();
        return Optional.of(r);
    }

    public <R> Optional<R> catchAngLog(Supplier<R> supplier) {
        return catchAngLog(supplier, Logger::error);
    }

}
