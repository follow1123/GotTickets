package com.yang.gotTickets.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yang.gotTickets.bean.table.TResource;
import com.yang.gotTickets.service.CacheService;
import com.yang.gotTickets.util.result.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @auther YF
 * @create 2021-05-26-15:12
 */
@RestController
@Api(tags = "缓存相关操作")
@Setter(onMethod_ = {@Autowired})
@RequestMapping("/cache")
public class CacheController {

    private CacheService cacheService;

    @GetMapping("/getValue")
    @ApiOperation("根据主键获取值")
    public R<?> getValue(
        @ApiParam(value = "key",required = true) @RequestParam String key
    ){
        return R.data(cacheService.getValue(key));
    }

    @GetMapping("/mkdir")
    @ApiOperation("创建文件夹")
    public void mkdir(
//            @ApiParam(value = "dirNames",required = true)  String[] dirNames
    ){
        cacheService.mkdir("A", "B", "C");
    }
}
