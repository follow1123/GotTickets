package com.yang.gotTickets.controller;

import com.yang.gotTickets.service.SpringIocService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @auther YF
 * @create 2021-06-07-10:54
 */
@RestController
@Api(tags = "spring 容器相关操作")
@Setter(onMethod_ = {@Autowired})
@RequestMapping("/spring-ioc")
public class SpringIocController {

    private SpringIocService springIocService;


    @GetMapping("/injection")
    @ApiOperation("注入一个对象")
    public void mkdir(
    ){
        springIocService.injection();
    }



}
