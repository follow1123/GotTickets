package com.yang.gotTickets.bean;

import com.baomidou.mybatisplus.annotation.TableField;
import com.yang.gotTickets.bean.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpMethod;

/**
 * <p>
 * 资源表
 * </p>
 *
 * @author YF
 * @since 2021-05-06
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="TResource对象", description="资源表")
public class TResource extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "资源（接口）")
    private String resourceUrl;

    @ApiModelProperty(value = "描述")
    private String description;

    @ApiModelProperty(value = "权限编码")
    private String authCode;

}
