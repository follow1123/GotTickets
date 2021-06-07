package com.yang.gotTickets.bean.table;

import com.yang.gotTickets.bean.table.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 枚举属性和值对应关系表
 * </p>
 *
 * @author YF
 * @since 2021-06-03
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="TEnumFieldMap对象", description="枚举属性和值对应关系表")
public class TEnumFieldMap extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "字段枚举的id")
    private Long enumId;

    @ApiModelProperty(value = "描述")
    private String description;


}
