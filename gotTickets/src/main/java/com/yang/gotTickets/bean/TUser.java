package com.yang.gotTickets.bean;

import com.baomidou.mybatisplus.annotation.TableField;
import com.yang.gotTickets.bean.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;

/**
 * <p>
 * 用户
 * </p>
 *
 * @author YF
 * @since 2021-05-06
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="TUser对象", description="用户")
public class TUser extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "账户名称")
    private String accountName;

    @ApiModelProperty(value = "密码")
    private String password;

    @ApiModelProperty(value = "姓名")
    private String realName;

    @ApiModelProperty(value = "身份证号")
    private String idNumber;

    @ApiModelProperty(value = "性别(0 女，1 男)")
    private Integer sex;

    @ApiModelProperty(value = "手机号")
    private String phone;

    @ApiModelProperty(value = "角色id")
    private Long roleId;

}
