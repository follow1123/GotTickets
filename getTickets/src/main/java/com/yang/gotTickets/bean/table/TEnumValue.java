package com.yang.gotTickets.bean.table;

import com.yang.gotTickets.bean.table.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

/**
 * <p>
 * 用户
 * </p>
 *
 * @author YF
 * @since 2021-05-24
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="TEnumValue对象", description="用户")
public class TEnumValue extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "字段枚举的id")
    private Long enumId;

    @ApiModelProperty(value = "枚举字段对应的key")
    private Byte enumKey;

    @ApiModelProperty(value = "枚举字段对应的value")
    private String enumValue;


}
