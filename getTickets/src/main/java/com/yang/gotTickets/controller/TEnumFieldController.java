package com.yang.gotTickets.controller;


import com.yang.gotTickets.service.TEnumFieldService;
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
 * <p>
 * 用户 前端控制器
 * </p>
 *
 * @author YF
 * @since 2021-05-24
 */
@RestController
@Api(tags = "枚举字段相关操作")
@Setter(onMethod_ = {@Autowired})
@RequestMapping("/t-enum-field")
public class TEnumFieldController {

    private TEnumFieldService enumFieldService;

    @GetMapping("/reset")
    @ApiOperation("重置枚举字段表")
    public R<Boolean> reset() {
        return R.isSuccess(enumFieldService.resetEnumField());
    }

    @GetMapping("/getCommentByEnumId")
    @ApiOperation("根据枚举id获取枚举字段的注释")
    public R<String> getCommentByEnumId(
            @ApiParam(value = "枚举id", required = true) @RequestParam Long enumId
    ) {
        return R.data(enumFieldService.getCommentByEnumId(enumId));
    }

    @GetMapping("/addEnumValue")
    @ApiOperation("添加枚举属性对应的枚举值")
    public R<Boolean> addEnumValue(
            @ApiParam(value = "枚举id", required = true) @RequestParam Long enumId,
            @ApiParam(value = "key", required = true) @RequestParam Byte key,
            @ApiParam(value = "value", required = true) @RequestParam String value
    ) {
        return R.isSuccess(enumFieldService.addEnumValue(enumId, key, value));
    }

    @GetMapping("/removeEnumValue")
    @ApiOperation("移除枚举属性对应的枚举值")
    public R<Boolean> removeEnumValue(
            @ApiParam(value = "枚举id", required = true) @RequestParam Long enumId,
            @ApiParam(value = "key", required = true) @RequestParam Byte key
    ) {
        return R.isSuccess(enumFieldService.removeEnumValue(enumId, key));
    }

}
