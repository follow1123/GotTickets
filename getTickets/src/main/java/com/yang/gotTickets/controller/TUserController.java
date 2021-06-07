package com.yang.gotTickets.controller;


import cn.hutool.core.text.StrSpliter;
import com.yang.gotTickets.annotation.authority.NoToken;
import com.yang.gotTickets.service.TUserService;
import com.yang.gotTickets.util.result.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

/**
 * <p>
 * 用户 前端控制器
 * </p>
 *
 * @author YF
 * @since 2021-05-15
 */
@RestController
@Api(tags = "用户操作")
@RequestMapping("/t-user")
@Setter(onMethod_ = {@Autowired})
public class TUserController {


    private TUserService userService;

    @NoToken
    @PostMapping("/login")
    @ApiOperation(value = "登录")
    public R<?> login(
            @ApiParam(value = "用户名", required = true) @RequestParam String username,
            @ApiParam(value = "密码", required = true) @RequestParam String password
    ){
        return R.data(userService.login(username, password));
    }

    @NoToken
    @PostMapping("/allocRole")
    @ApiOperation(value = "分配角色")
    public R<?> allocRole(
            @ApiParam(value = "用户id", required = true) @RequestParam Long userId,
            @ApiParam(value = "角色Id", required = true) @RequestParam String roleIds
    ){
        ArrayList<Long> collect = StrSpliter.split(roleIds, ",", true, true).stream().mapToLong(Long::parseLong).collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
        return R.data(userService.allocRole(userId, collect));
    }



}
