package com.yang.gotTickets.handler;

import cn.hutool.core.exceptions.ExceptionUtil;
import com.yang.gotTickets.exception.BusException;
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
        e.printStackTrace();
        String message = ExceptionUtil.getSimpleMessage(e);
        log.error(message);
        return R.fail(message);
    }

    @ExceptionHandler(BusException.class)
    public R<Boolean> handleException(BusException e) {
        String message = ExceptionUtil.getSimpleMessage(e);
        log.error(message);
        return R.fail(message, e.getCode());
    }

}
