package com.yang.gotTickets.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.yang.gotTickets.bean.table.TEnumField;
import com.yang.gotTickets.bean.table.TEnumFieldMap;
import com.yang.gotTickets.bean.table.TEnumValue;
import com.yang.gotTickets.exception.BusException;
import com.yang.gotTickets.mapper.TEnumFieldMapper;
import com.yang.gotTickets.service.TEnumFieldMapService;
import com.yang.gotTickets.service.TEnumFieldService;
import com.yang.gotTickets.service.TEnumValueService;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * <p>
 * 用户 服务实现类
 * </p>
 *
 * @author YF
 * @since 2021-05-24
 */
@Service
@Setter(onMethod_ = {@Autowired})
public class TEnumFieldServiceImpl extends BaseServiceImpl<TEnumFieldMapper, TEnumField> implements TEnumFieldService {

        private TEnumFieldMapper enumFieldMapper;

    private TEnumValueService enumValueService;

    private TEnumFieldMapService enumFieldMapService;

    @Setter
    private Pattern pattern = Pattern.compile("([0-9]+):([\\u4e00-\\u9fa5 \\u4dae\\uE863\\w]+)");

    @Setter
    private AtomicLong enumId = new AtomicLong(1);

    @Transactional
    public boolean resetEnumField(String... ignoreFiledName) {
        enumId.set(1);
        enumFieldMapper.truncateTable("t_enum_field");
        enumFieldMapper.truncateTable("t_enum_field_map");
        enumFieldMapper.truncateTable("t_enum_value");
        List<TEnumField> enumFields = enumFieldMapper.listEnumTables(null);
        enumFields.removeIf(e -> Arrays.asList("t_enum_field", "t_enum_value", "t_enum_field_map").contains(e.getName()));
        List<TEnumField> enumFieldList = new ArrayList<>();
        Long curTableId = 0L;
        for (TEnumField enumField : enumFields) {
            enumField.setPId(0L);
            if (save(enumField)) curTableId = enumField.getId();
            List<Map<String, String>> maps = enumFieldMapper.listEnumFields(enumField.getName());
            List<Map<String, String>> enumFieldValue = maps.stream().filter(m ->
                    m.get("type").startsWith("tinyint") && !ListUtil.toList(ignoreFiledName).contains(m.get("name"))
            ).collect(Collectors.toList());
            for (Map<String, String> fieldValue : enumFieldValue) {
                String name = fieldValue.get("name");
                String comment = fieldValue.get("comment");
                Long enumId = getEnumId(comment);
                enumFieldList.add(TEnumField.builder()
                        .pId(curTableId)
                        .enumId(enumId)
                        .name(name)
                        .description(comment)
                        .build());
            }
        }
        return saveBatch(enumFieldList);
    }

    @Transactional
    public Long getEnumId(String comment) {
        ArrayList<TEnumValue> enumValues = new ArrayList<>();
        Matcher matcher = pattern.matcher(comment);
        String decs = pattern.split(comment)[0];
        enumFieldMapService.save(TEnumFieldMap.builder()
                .enumId(enumId.longValue())
                .description(decs.trim())
                .build());
        while (matcher.find()) {
            enumValues.add(TEnumValue.builder()
                    .enumId(enumId.longValue())
                    .enumKey(Byte.valueOf(matcher.group(1)))
                    .enumValue(matcher.group(2))
                    .build());
        }
        enumValueService.saveBatch(enumValues);
        return enumId.getAndIncrement();
    }


    @Transactional
    public boolean resetEnumField() {
        return this.resetEnumField("is_deleted");
    }

    @Transactional
    public boolean addEnumValue(Long enumId, Byte key, String value) {
        Map<Byte, String> map = getCommentMapByEnumId(enumId).orElseThrow(() -> new BusException("枚举类型不存在"));
        if (map.containsKey(key)) throw new BusException("该枚举类型已经有这个key");
        boolean save = enumValueService.save(TEnumValue.builder()
                .enumId(enumId)
                .enumKey(key)
                .enumValue(value)
                .build());
        return save && syncEnumValueAndComment(enumId);
    }

    @Transactional
    public boolean removeEnumValue(Long enumId, Byte key) {
        LambdaUpdateWrapper<TEnumValue> wrapper = Wrappers.lambdaUpdate(TEnumValue.class)
                .eq(TEnumValue::getEnumId, enumId)
                .eq(TEnumValue::getEnumKey, key);
        return enumValueService.remove(wrapper) && syncEnumValueAndComment(enumId);
    }

    @Transactional
    public boolean syncEnumValueAndComment(Long enumId) {
        List<TEnumField> enumFields = list();
        List<TEnumField> tables = enumFields.stream().filter(e -> e.getPId() == 0).collect(Collectors.toList());
        enumFields.removeIf(e -> e.getPId() == 0);
        if (enumId != null) enumFields.removeIf(e -> !e.getEnumId().equals(enumId));
        for (TEnumField enumField : enumFields) {
            Optional<TEnumField> first = tables.stream().filter(e -> e.getId().equals(enumField.getPId())).findFirst();
            first.ifPresent(tEnumField -> enumField.setTableName(tEnumField.getName()));
            enumFieldMapper.updateColumnComment(enumField.getTableName(), enumField.getName(), getCommentByEnumId(enumField.getEnumId()));
        }
        LambdaQueryWrapper<TEnumField> wrapper = Wrappers.lambdaQuery(TEnumField.class)
                .eq(enumId != null, TEnumField::getEnumId, enumId);
        List<TEnumField> list = list(wrapper);
        String comment = getCommentByEnumId(enumId);
        for (TEnumField enumField : list) {
            enumField.setDescription(comment);
        }
        return updateBatchById(list);
    }

    @Transactional
    public Optional<Map<Byte, String>> getCommentMapByEnumId(Long enumId) {
        LambdaUpdateWrapper<TEnumValue> wrapper = Wrappers.lambdaUpdate(TEnumValue.class)
                .eq(TEnumValue::getEnumId, enumId);
        HashMap<Byte, String> map = new HashMap<>();
        List<TEnumValue> list = enumValueService.list(wrapper);
        if (CollUtil.isEmpty(list)) return Optional.empty();
        for (TEnumValue enumValue : list) {
            map.put(enumValue.getEnumKey(), enumValue.getEnumValue());
        }
        return Optional.of(map);
    }

    @Transactional
    public String getCommentByEnumId(Long enumId) {
        LambdaUpdateWrapper<TEnumFieldMap> wrapper = Wrappers.lambdaUpdate(TEnumFieldMap.class)
                .eq(TEnumFieldMap::getEnumId, enumId);
        StringBuilder builder = new StringBuilder();
        TEnumFieldMap one = enumFieldMapService.getOne(wrapper);
        if (one != null) builder.append(one.getDescription().trim()).append(" ");
        getCommentMapByEnumId(enumId).ifPresent(m ->
                m.forEach((k, v) -> builder.append(k).append(":").append(v).append(",")));
        return builder.substring(0, builder.length() - 1);
    }

    @Override
    @Transactional
    public Map<Byte, String> getEnumValueMap(String name) {
        LambdaUpdateWrapper<TEnumFieldMap> wrapper = Wrappers.lambdaUpdate(TEnumFieldMap.class)
                .eq(TEnumFieldMap::getDescription, name);
        TEnumFieldMap one = enumFieldMapService.getOne(wrapper);
        if(one == null) throw new BusException("没有这个枚举类型");
        return getCommentMapByEnumId(one.getEnumId()).orElseThrow(()-> new BusException("没有这个枚举类型"));
    }
}
