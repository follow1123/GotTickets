package com.yang.gotTickets.service;

/**
 * @auther YF
 * @create 2021-05-26-15:14
 */
public interface CacheService {

    String getValue(String key);

    void mkdir(String... dirName);
}
