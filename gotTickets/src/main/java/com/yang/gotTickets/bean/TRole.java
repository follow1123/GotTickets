package com.yang.gotTickets.bean;

import com.yang.gotTickets.bean.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 角色表
 * </p>
 *
 * @author YF
 * @since 2021-05-06
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="TRole对象", description="角色表")
public class TRole extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "角色编码")
    private String code;

    @ApiModelProperty(value = "角色名称")
    private String name;

    @ApiModelProperty(value = "角色描述")
    private String description;

    @ApiModelProperty(value = "权限编码")
    private String authCode;


}
