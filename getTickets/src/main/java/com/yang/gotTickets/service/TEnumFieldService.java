package com.yang.gotTickets.service;

import com.yang.gotTickets.bean.table.TEnumField;
import com.yang.gotTickets.service.BaseService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 用户 服务类
 * </p>
 *
 * @author YF
 * @since 2021-05-24
 */
public interface TEnumFieldService extends BaseService<TEnumField> {

    boolean resetEnumField();

    String getCommentByEnumId(Long enumId);

    boolean addEnumValue(Long enumId, Byte key, String value);

    boolean removeEnumValue(Long enumId, Byte key);

    Map<Byte, String> getEnumValueMap(String name);

}
