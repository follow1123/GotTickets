package com.yang.gotTickets.bean;

import com.yang.gotTickets.bean.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 城市表
 * </p>
 *
 * @author YF
 * @since 2021-05-06
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="TCity对象", description="城市表")
public class TCity extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "编码")
    private String code;

    @ApiModelProperty(value = "城市名称")
    private String name;

    @ApiModelProperty(value = "省份id")
    private Long provinceId;


}
