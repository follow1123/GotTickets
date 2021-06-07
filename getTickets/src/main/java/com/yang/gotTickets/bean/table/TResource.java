package com.yang.gotTickets.bean.table;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

/**
 * <p>
 * 资源表
 * </p>
 *
 * @author YF
 * @since 2021-05-15
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="TResource对象", description="资源表")
public class TResource extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "资源（接口）")
    private String resourceUrl;

    @ApiModelProperty(value = "请求方式(0:GET,1:POST,2:PUT,3:DELETE)")
    private Byte method;

    @ApiModelProperty(value = "描述")
    private String description;

    @ApiModelProperty(value = "权限编码")
    private String authCode;

    @ApiModelProperty(hidden = true)
    @TableField(exist = false)
    private Boolean haveAuth;


}
