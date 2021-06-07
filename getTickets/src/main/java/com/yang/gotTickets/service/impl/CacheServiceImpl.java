package com.yang.gotTickets.service.impl;

import com.yang.gotTickets.service.CacheService;
import com.yang.gotTickets.util.redisUtil.RedisData;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @auther YF
 * @create 2021-05-26-15:14
 */
@Service
@Setter(onMethod_ = {@Autowired})
public class CacheServiceImpl implements CacheService {

    @Override
    public String getValue(String key) {
        return null;
    }

    @Override
    public void mkdir(String... dirName) {
//        redisUtil.mkdir(null, dirName);
    }
}
