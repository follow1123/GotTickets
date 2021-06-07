package com.yang.gotTickets.controller;


import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yang.gotTickets.annotation.authority.AuthFilter;
import com.yang.gotTickets.annotation.cache.Cached;
import com.yang.gotTickets.bean.table.TResource;
import com.yang.gotTickets.service.TResourceService;
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

import java.util.List;

/**
 * <p>
 * 资源表 前端控制器
 * </p>
 *
 * @author YF
 * @since 2021-05-15
 */
@RestController
@Api(tags = "接口资源相关操作")
@Setter(onMethod_ = {@Autowired})
@RequestMapping("/t-resource")
public class TResourceController {

    private TResourceService resourceService;

    @ApiOperation("资源列表")
    @GetMapping("/listResource")
    public R<List<TResource>> resourceList(){
        return R.data(resourceService.list());
    }

    @ApiOperation("根据id获取资源列表")
    @GetMapping("/getById")
    @AuthFilter
    public R<TResource> getById(
            @ApiParam(value = "资源id", required = true) @RequestParam Long id
    ){
        System.out.println(id);
        return R.data(resourceService.getById(id));
    }


    @AuthFilter
    @ApiOperation("获取资源列表")
    @GetMapping("/listResourceById")
    public R<Page<TResource>> listResource(
            @ApiParam(value = "资源id") @RequestParam(required = false) Long id,
            @ApiParam(value = "当前页数", required = true) @RequestParam Integer pageNumber,
            @ApiParam(value = "总页数", required = true) @RequestParam Integer pageSize
    ){
        LambdaUpdateWrapper<TResource> wrapper = Wrappers.lambdaUpdate(TResource.class).eq(id != null, TResource::getId, id);
        return R.data(resourceService.page(new Page<>(pageNumber, pageSize),wrapper));
    }

}
