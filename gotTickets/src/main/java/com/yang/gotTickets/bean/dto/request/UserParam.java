package com.yang.gotTickets.bean.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @auther YF
 * @create 2021-04-27-15:47
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserParam  extends BaseRequestParam{

    @ApiModelProperty(value = "姓名")
    private String realName;

    @ApiModelProperty(value = "身份证号")
    private String idNumber;

    @ApiModelProperty(value = "性别(0 女，1 男)")
    private Integer sex;

}
