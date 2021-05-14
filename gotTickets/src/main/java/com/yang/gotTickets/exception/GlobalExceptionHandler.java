package com.yang.gotTickets.exception;

import cn.hutool.core.exceptions.ExceptionUtil;
import com.yang.gotTickets.util.result.R;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @auther YF
 * @create 2021-04-20-15:14
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Throwable.class)
    public R<Boolean> handleException(Throwable e) {
        String message = ExceptionUtil.getMessage(e);
        log.error(message, e);
        return R.fail(message);
    }

    @ExceptionHandler(BusException.class)
    public R<Boolean> handleException(BusException e) {
        String message = ExceptionUtil.getMessage(e);
        log.error(message, e);
        return R.fail(e.getMessage(), e.getCode());
    }



}
