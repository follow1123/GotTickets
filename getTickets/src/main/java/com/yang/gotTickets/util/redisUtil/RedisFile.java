package com.yang.gotTickets.util.redisUtil;

import java.util.Optional;

/**
 * @auther YF
 * @create 2021-05-31-9:47
 */
public interface RedisFile {

    /**
     * 创建redis内的文件夹，多个则是多级文件夹
     * redis文件夹默认就是key字段以 : 分割的字符串，一个 : 则代表一个文件夹，所以必须将文件夹字段单独存放
     * @param expired 字段过期时间
     * @param dirName 文件夹名称1
     */
    void mkdir(Long expired, String... dirName);

    /**
     * 重载，默认该字段不过期
     * @param dirName 文件夹名称
     */
    void mkdir(String... dirName);

    /**
     * 获取文件夹，没有改文件夹则使用默认文件夹
     * @param dirName 文件夹名
     * @return 文件夹绝对路径
     */
    Optional<String> getDir(String dirName);

    /**
     * 获取项目名称
     * @return 项目名称
     */
    String projectName();


    /**
     * 获取由文件夹组成的redis key
     * @param dir 文件夹名称
     * @param key 主键名称
     * @param isSet 是否是获取时3的方法
     * @return 由文件夹组成的redis key
     */
    Optional<String> getDirKey(String dir, String key, boolean isSet);


    /**
     * 判断redis服务区内是否存在该主键
     * @param key 主键名
     * @return 是否存在
     */
    boolean hasKey(String key);

    /**
     * 使用redis特殊的文件夹方式拼接文件夹
     * @param dirName 文件夹名称
     * @param key 主键名
     * @return 连接后的主键
     */
    String concatDirName(String dirName, String key);


}
