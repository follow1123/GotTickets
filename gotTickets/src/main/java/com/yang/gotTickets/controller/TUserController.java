package com.yang.gotTickets.controller;


import com.yang.gotTickets.annotation.NoToken;
import com.yang.gotTickets.annotation.Authority;
import com.yang.gotTickets.bean.TUser;
import com.yang.gotTickets.bean.dto.LoginUserDetailsDto;
import com.yang.gotTickets.service.TUserService;
import com.yang.gotTickets.util.constant.CRole;
import com.yang.gotTickets.util.result.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 用户 前端控制器
 * </p>
 *
 * @author YF
 * @since 2021-05-06
 */
@RestController
@Api(tags = "用户相关操作")
@RequestMapping("/t-user")
@Setter(onMethod_ = {@Autowired})
public class TUserController {

    private TUserService userService;

    @GetMapping("/getUser")
    @NoToken
    @Authority(CRole.ADMIN)
    @ApiOperation(value = "获取所有权限")
    public R<TUser> getUser(String accountName){
        return R.data(null);
    }

    @GetMapping("/getUser1")
    @ApiOperation(value = "获取用户1")
    public R<Boolean> getUser1(){
        return R.success();
    }

    @NoToken
    @PostMapping("/login")
    @ApiOperation(value = "登录")
    public R<LoginUserDetailsDto> login(
            @ApiParam(value = "用户名", required = true) @RequestParam String username,
            @ApiParam(value = "密码", required = true) @RequestParam String password
    ){
        return R.data(userService.login(username, password));
    }
}
