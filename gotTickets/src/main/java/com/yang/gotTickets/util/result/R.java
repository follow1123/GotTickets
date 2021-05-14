package com.yang.gotTickets.util.result;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.http.HttpStatus;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @auther YF
 * @create 2021-04-20-15:16
 */
@Data
public class R<T> implements Serializable {

    public static final String DEFAULT_SUCCESS_MESSAGE = "操作成功";
    public static final String DEFAULT_FAILURE_MESSAGE = "操作失败";
    public static final String DEFAULT_FAILURE_BUSINESS_MESSAGE = "业务异常";


    @ApiModelProperty(value = "时间戳")
    private String timestamp;

    @ApiModelProperty(value = "消息")
    private String message;

    @ApiModelProperty(value = "响应码")
    private int code;

    @ApiModelProperty(value = "数据")
    private T data;


    R(T data, String message, int code) {
        this.message = message;
        this.data = data;
        this.code = code;
        this.timestamp = DateUtil.format(LocalDateTime.now(), DatePattern.NORM_DATETIME_PATTERN);
    }


    public static <T> R<T> data(T data, String message, int code) {
        return new R<T>(data, message, code);
    }

    public static <T> R<T> data(T data, String message) {
        return data(data, message, HttpStatus.HTTP_OK);
    }

    public static <T> R<T> data(T data) {
        return data(data, DEFAULT_SUCCESS_MESSAGE, HttpStatus.HTTP_OK);
    }

    public static R<Boolean> isSuccess(boolean success) {
        return success ? success() : fail(DEFAULT_FAILURE_MESSAGE);
    }


    public static R<Boolean> fail(String message, int code) {
        return data(false, message, code);
    }

    public static R<Boolean> fail(String message) {
        return fail(message, HttpStatus.HTTP_BAD_REQUEST);
    }

    public static R<Boolean> fail() {
        return fail(DEFAULT_FAILURE_BUSINESS_MESSAGE);
    }

    public static R<Boolean> success(String message, int code) {
        return data(true, message, code);
    }

    public static R<Boolean> success(String message) {
        return success(message, HttpStatus.HTTP_OK);
    }

    public static R<Boolean> success() {
        return success(DEFAULT_SUCCESS_MESSAGE);
    }

}
