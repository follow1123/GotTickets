package com.yang.gotTickets.bean.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @auther YF
 * @create 2021-04-27-15:08
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CityParam  extends PageRequestParam{

    @ApiModelProperty(value = "城市名")
    private String name;

    @ApiModelProperty(value = "城市编码")
    private String code;

}
