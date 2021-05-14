package com.yang.gotTickets.bean.dto.request;

/**
 * @auther YF
 * @create 2021-04-27-15:43
 */
public class BaseRequestParam implements Fillable{
    @Override
    public <T> T fillBean(Class<T> type) {
        return fillBean(this, type);
    }

    @Override
    public <T> T fillBean(Class<T> type, String... ignoreFields) {
        return fillBean(this, type, ignoreFields);
    }
}
