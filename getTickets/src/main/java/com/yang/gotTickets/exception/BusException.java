package com.yang.gotTickets.exception;


import cn.hutool.http.HttpStatus;
import com.yang.gotTickets.util.result.R;

/**
 * @auther YF
 * @create 2021-04-19-18:41
 */
public class BusException extends RuntimeException {

    private Integer code;

    public BusException(String message) {
        this(message, HttpStatus.HTTP_BAD_REQUEST);
    }

    public BusException() {
        this(R.DEFAULT_FAILURE_BUSINESS_MESSAGE);
    }

    public Integer getCode() {
        return code;
    }

    public BusException(String message, int code) {
        super(message);
        this.code = code;
    }
}
