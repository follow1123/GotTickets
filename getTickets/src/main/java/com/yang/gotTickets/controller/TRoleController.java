package com.yang.gotTickets.controller;


import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.yang.gotTickets.annotation.authority.NoToken;
import com.yang.gotTickets.annotation.cache.CacheInterrupter;
import com.yang.gotTickets.bean.table.TRole;
import com.yang.gotTickets.service.TRoleService;
import com.yang.gotTickets.util.AuthUtil;
import com.yang.gotTickets.util.redisUtil.RedisData;
import com.yang.gotTickets.util.result.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


/**
 * <p>
 * 角色表 前端控制器
 * </p>
 *
 * @author YF
 * @since 2021-05-17
 */
@NoToken
@RestController
@Api(tags = "角色操作")
@Setter(onMethod_ = {@Autowired})
@RequestMapping("/t-role")
public class TRoleController {


    private TRoleService roleService;

    private AuthUtil authUtil;

    private RedisTemplate<String, String> redisTemplate;

    private RedisData redisData;


    @GetMapping("/updateById")
    @ApiOperation(value = "根据id修改角色权限信息")
    public R<Boolean> updateById(@ApiParam(value = "角色id", required = true) @RequestParam Long id) {
        return R.data(roleService.updateRoleCodeById(id));
    }


    @GetMapping("/listRole")
    @ApiOperation(value = "查询所有角色信息")
    public R<List<TRole>> listRole() {
        System.out.println(roleService);
        TimeInterval timer = DateUtil.timer();
        timer.start();
        List<TRole> tRoles = roleService.listRole();
        System.out.println( "===========" + timer.interval());
        return R.data(tRoles);
    }


    @PutMapping("/insertRole")
    @ApiOperation(value = "新建角色")
    public R<Boolean> insertRole(
            @ApiParam(value = "角色名称", required = true) @RequestParam String name,
            @ApiParam(value = "角色描述") @RequestParam(required = false) String description
    ) {
        return R.data(roleService.insertRole(name, description));
    }

}
