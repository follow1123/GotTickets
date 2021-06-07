package com.yang.gotTickets.util.redisUtil;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;

/**
 * @auther YF
 * @create 2021-05-31-10:30
 */
public interface RedisData {

    /**
     * 将数据存入redis数据库内的通用方法
     * @param dir 文件夹名称
     * @param key 主键名
     * @param consumer 操作
     * @param expired 过期时间
     */
    void put(String dir, String key, Consumer<String> consumer, Long expired);


    /**
     * 将数据以值的的形式存入redis数据库
     * 类型为引用类型则转换为json字符串
     * @param dir 文件夹名称
     * @param key 主键名
     * @param value 值
     * @param expired 过期时间
     */
    void putValue(String dir, String key, Object value, Long expired);

    /**
     * 将一个list存储为redis数据库内的集合类型
     * @param dir 文件夹名称
     * @param key 主键名
     * @param value 值
     * @param expired 过期时间
     */
    void putList(String dir, String key, List<?> value, Long expired);

    /**
     * 将一个set存储为redis数据库内的集合类型
     * @param dir 文件夹名称
     * @param key 主键名
     * @param value 值
     * @param expired 过期时间
     */
    void putSet(String dir, String key, Set<?> value, Long expired);

    /**
     * 将一个map存储为redis数据库内的hash类型
     * @param dir 文件夹名称
     * @param key 主键名
     * @param value 值
     * @param expired 过期时间
     */
    void putMap(String dir, String key, Map<String, ?> value, Long expired);

    /**
     * 获取redis的的值对象
     * @param dir 文件夹名称
     * @param key 主键名
     * @param valType 值的class
     * @param <V> 值的类型
     * @return 值
     */
    <V> Optional<V> getValue(String dir, String key, Class<V> valType);

    /**
     * 获取redis数据库内的list集合类型
     * @param dir 文件夹名称
     * @param key 主键名
     * @param valType 集合内值的class
     * @param <V> 集合内值的类型
     * @return list集合
     */
    <V> List<V> getList(String dir, String key, Class<V> valType);

    /**
     * 获取redis数据库内的set集合类型
     * @param dir 文件夹名称
     * @param key 主键名
     * @param valType 集合内值的class
     * @param <V> 集合内值的类型
     * @return set集合
     */
    <V> Set<V> getSet(String dir, String key, Class<V> valType);

    /**
     * 获取redis数据库内的hash类型
     * @param dir 文件夹名称
     * @param key 主键名
     * @param valType map内value的class，map的key默认使用字符串
     * @param <V> map内value的类型
     * @return map集合
     */
    <V> Map<String, V> getMap(String dir, String key, Class<V> valType);

    /**
     * 移除一个属性
     * @param dir 文件夹名称
     * @param key 主键名
     */
    void remove(String dir, String key);
}
