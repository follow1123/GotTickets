package com.yang.gotTickets.util.redisUtil;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.*;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * @auther YF
 * @create 2021-05-31-10:30
 */
@Slf4j
@Component
@Setter(onMethod_ = {@Autowired})
public class RedisDataImpl implements RedisData {

    private RedisTemplate<String, String> redisTemplate;

    private RedisFile redisFile;

    private String parseToString(Object value) {
        JSON parse = JSONUtil.parse(value, JSONConfig.create().setIgnoreNullValue(false).setOrder(true));
        return JSONUtil.toJsonStr(parse);
    }

    @SneakyThrows
    @SuppressWarnings("unchecked")
    private <V> V parseToObject(String value, Class<V> valType) {
        if (JSONUtil.isJsonObj(value)) {
            JSON parse = JSONUtil.parse(value, JSONConfig.create().setOrder(true));
            return JSONUtil.toBean(parse, valType, true);
        }
        if (JSONUtil.isJsonArray(value)) {
            ArrayList<Map<String, Object>> valueMaps = new ArrayList<>();
            JSONArray jsonArray = JSONUtil.parseArray(value, JSONConfig.create().setOrder(true));
            for (int i = 0; i < jsonArray.size(); i++) {
                valueMaps.add(jsonArray.get(i, JSONObject.class));
            }
            return (V) valueMaps;
        }
        return BeanUtil.toBean(valType, valType);
    }

    @Override
    public void put(String dir, String key, Consumer<String> consumer, Long expired) {
        Assert.isFalse(expired != null && expired < 0, "过期时间必须为正整数，默认 4小时后过期：{}", expired);
        Optional<String> dirKey = redisFile.getDirKey(dir, key, true);
        dirKey.ifPresent(k -> {
            consumer.accept(k);
            if (expired != null)
                redisTemplate.expire(k, expired, TimeUnit.MILLISECONDS);
        });
    }

    public void putValue(String dir, String key, Object value, Long expired) {
        put(dir, key, k -> redisTemplate.opsForValue().set(k, parseToString(value)), expired);
    }

    public void putList(String dir, String key, List<?> value, Long expired) {
        put(dir, key, k -> redisTemplate.opsForList().leftPushAll(k, value.stream().map(this::parseToString).collect(Collectors.toList())), expired);
    }

    public void putSet(String dir, String key, Set<?> value, Long expired) {
        put(dir, key, k -> redisTemplate.opsForSet().add(k, value.stream().map(this::parseToString).toArray(String[]::new)), expired);
    }

    public void putMap(String dir, String key, Map<String, ?> value, Long expired) {
        put(dir, key, k -> {
            HashMap<String, Object> map = new HashMap<>();
            value.forEach((ky, v) -> map.put(ky, parseToString(v)));
            redisTemplate.opsForHash().putAll(k, map);
        }, expired);
    }

    public <V> Optional<V> getValue(String dir, String key, Class<V> valType) {
        Optional<String> dirKey = redisFile.getDirKey(dir, key, false);
        if (dirKey.isPresent()) {
            String value = redisTemplate.opsForValue().get(dirKey.get());
            return Optional.of(parseToObject(value, valType));
        }
        return Optional.empty();
    }

    public <V> List<V> getList(String dir, String key, Class<V> valType) {
        List<V> valResult = new ArrayList<>();
        Optional<String> dirKey = redisFile.getDirKey(dir, key, false);
        if (dirKey.isPresent()) {
            Long size = redisTemplate.opsForList().size(dirKey.get());
            if (size == null) return valResult;
            List<String> range = redisTemplate.opsForList().range(dirKey.get(), 0, size);
            if (range == null) return valResult;
            valResult = range.stream().map(v -> parseToObject(v, valType)).collect(Collectors.toList());
        }
        return valResult;
    }

    public <V> Set<V> getSet(String dir, String key, Class<V> valType) {
        Set<V> valResult = new HashSet<>();
        Optional<String> dirKey = redisFile.getDirKey(dir, key, false);
        if (dirKey.isPresent()) {
            Set<String> members = redisTemplate.opsForSet().members(dirKey.get());
            if (members == null) return valResult;
            valResult = members.stream().map(v -> parseToObject(v, valType)).collect(Collectors.toSet());
        }
        return valResult;
    }

    public <V> Map<String, V> getMap(String dir, String key, Class<V> valType) {
        HashMap<String, V> valResult = new HashMap<>();
        Optional<String> dirKey = redisFile.getDirKey(dir, key, false);
        if (dirKey.isPresent()) {
            Map<Object, Object> entries = redisTemplate.opsForHash().entries(dirKey.get());
            entries.forEach((k, v) -> valResult.put(String.valueOf(k), parseToObject(String.valueOf(v), valType)));
        }
        return valResult;
    }

    public void remove(String dir, String key) {
        Optional<String> dirKey = redisFile.getDirKey(dir, key, false);
        if (dirKey.isPresent()) {
            Boolean delete = redisTemplate.delete(dirKey.get());
            if (delete != null) {
                if (delete) log.info(StrUtil.join("", "主键", key, "移除成功"));
                else log.info(StrUtil.join("", "主键", key, "移除失败"));
            }
        }
    }


}
