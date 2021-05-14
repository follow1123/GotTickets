package com.yang.gotTickets.controller;


import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import com.yang.gotTickets.annotation.NoToken;
import com.yang.gotTickets.annotation.Authority;
import com.yang.gotTickets.bean.TCity;
import com.yang.gotTickets.service.TCityService;
import com.yang.gotTickets.service.TProvinceService;
import com.yang.gotTickets.util.constant.CAuth;
import com.yang.gotTickets.util.constant.CRole;
import com.yang.gotTickets.util.result.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * <p>
 * 城市表 前端控制器
 * </p>
 *
 * @author YF
 * @since 2021-05-06
 */
@RestController
@Api(tags = "城市相关操作")
@RequestMapping("/t-city")
@Setter(onMethod_ = {@Autowired})
@Authority(CRole.DATA)
public class TCityController {


    @GetMapping("/getCity")
    @NoToken
    @Authority({CRole.TEST,CRole.ADMIN})
    @ApiOperation(value = "获取城市列表")
    public R<Boolean> getCity(){
        return R.success();
    }

    @GetMapping("/getCity1")
    @NoToken
    @ApiOperation(value = "获取城市列表1")
    public R<Boolean> getCity1(HttpServletRequest request,
                               HttpSession session){
        System.out.println(request);
        System.out.println(session);
        return R.success();
    }

    @GetMapping("/getCity2")
//    @NoToken
//    @Authority
    @ApiOperation(value = "获取城市列表2")
    public R<Boolean> getCity2(){
        return R.success();
    }

    @GetMapping("/getCity3")
//    @NoToken
//    @Authority
    @ApiOperation(value = "获取城市列表3")
    public R<Boolean> getCity3(){
        return R.success();
    }


    private TCityService cityService;

    private TProvinceService tProvinceService;

    @GetMapping("/getDetail")
    @NoToken
    @ApiOperation(value = "测试")
    @Transactional(readOnly = true)
    public R<Boolean> getDetail(){

        TimeInterval timer = DateUtil.timer();
        timer.start();
        cityService.list();
        tProvinceService.list();
        System.out.println(timer.interval());
        return R.success();
    }
}
