package com.yang.gotTickets.bean.table;

import com.baomidou.mybatisplus.annotation.TableField;
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
@ApiModel(value="TEnumField对象", description="用户")
public class TEnumField extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "表名或字段名")
    private String name;

    @ApiModelProperty(value = "表名", hidden = true)
    @TableField(exist = false)
    private String tableName;

    @ApiModelProperty(value = "父id，如果这条数据存储的信息是字段名的花这就对应该字段的表的id")
    private Long pId;

    @ApiModelProperty(value = "字段枚举的id")
    private Long enumId;

    @ApiModelProperty(value = "描述")
    private String description;

}
