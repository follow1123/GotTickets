package com.yang.gotTickets.util.redisUtil;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @auther YF
 * @create 2021-05-31-9:43
 * redis内创建文件夹操作
 */
@Slf4j
@Component
@Setter(onMethod_ = {@Autowired})
public class RedisFileImpl implements RedisFile {

    // redisTemplate对象，用以操作redis
    private RedisTemplate<String, String> redisTemplate;

    // 默认存储key文件夹的字段
    private final String redisDirName = "REDIS_DEPOSITORY_NAMES";

    private final String fileConnect = "/";

    private final String redisFileConnect = ":";

    @Override
    public String projectName() {
        String[] split = this.getClass().getPackage().getName().split("\\.");
        String projectName = split[split.length - 2];
        if (!getDir(projectName).isPresent()) mkdir(projectName);
        return projectName;
    }


    @Override
    public void mkdir(Long expired, String... dirName) {
        HashMap<String, String> dirs = new HashMap<>(dirName.length);
        LinkedList<String> dirNames = Arrays.stream(dirName).collect(Collectors.toCollection(LinkedList::new));
        do {
            dirs.put(dirNames.getLast(), ArrayUtil.join(dirNames.toArray(), "/"));
            dirNames.removeLast();
        } while (!dirNames.isEmpty());
        ValueOperations<String, String> value = redisTemplate.opsForValue();
        dirs.forEach((k, v) -> {
            if (expired != null) value.set(concatDirName(redisDirName, k), v, expired);
            else value.set(concatDirName(redisDirName, k), v);
            log.info("redis 创建文件夹：" + k + "，绝对路径为：" + v);
        });
    }

    @Override
    public void mkdir(String... dirName) {
        mkdir(null, dirName);
    }


    @Override
    public Optional<String> getDir(String dirName) {
        String dir = redisTemplate.opsForValue().get(StrUtil.join("", redisDirName, redisFileConnect,dirName));
        if (StrUtil.isBlank(dir)) return Optional.empty();
        return Optional.of(dir.replace(fileConnect, redisFileConnect));
    }

    @Override
    public String concatDirName(String dirName, String key) {
        if (StrUtil.isBlank(dirName)) throw new IllegalArgumentException("文件夹不能为空");
        Optional<String> dir = getDir(dirName);
        return dir.orElse(dirName) + redisFileConnect + key;
    }

    private String concatDirName(String key) {
        return concatDirName(null, key);
    }

    private boolean hasDir(String dirName) {
        if (StrUtil.isBlank(dirName)) dirName = projectName();
        return hasKey(concatDirName(redisDirName, dirName));
    }

    public boolean hasKey(String key) {
        Boolean hasKey = redisTemplate.hasKey(key);
        return hasKey != null && hasKey;
    }

    public Optional<String> getDirKey(String dir, String key, boolean isSet) {
        if (!hasDir(dir)) {
            if (isSet) mkdir(dir);
            log.warn(StrUtil.join("", "文件夹", dir, "不存在"));
            return Optional.empty();
        }
        String dirName = getDir(dir).orElse(projectName());
        String dirKey = concatDirName(dirName, key);
        boolean hasKey = hasKey(dirKey);
        if (isSet && hasKey){
            log.warn(StrUtil.join("", "主键", key, "在", dir, "文件夹下已存在"));
            return Optional.empty();
        }
        if (!isSet && !hasKey){
            log.warn(StrUtil.join("", "主键", key, "不存在"));
            return Optional.empty();
        }
        return Optional.of(dirKey);
    }
}
