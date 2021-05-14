package com.yang.gotTickets.bean.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @auther YF
 * @create 2021-04-26-8:51
 */
@Data
public class PageRequestParam implements Fillable {

    public PageRequestParam() {
        this(1, 10);
    }

    public PageRequestParam(Integer pageNumber, Integer pageSize) {
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
    }

    @ApiModelProperty(value = "当前页数")
    private Integer pageNumber;

    @ApiModelProperty(value = "总页数")
    private Integer pageSize;

    @Override
    public <T> T fillBean(Class<T> type) {
        return fillBean(this, type);
    }

    @Override
    public <T> T fillBean(Class<T> type, String... ignoreFields) {
        return fillBean(this, type, ignoreFields);
    }
}
