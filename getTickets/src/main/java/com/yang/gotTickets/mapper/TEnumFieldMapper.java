package com.yang.gotTickets.mapper;

import com.yang.gotTickets.bean.table.TEnumField;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import lombok.Setter;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 用户 Mapper 接口
 * </p>
 *
 * @author YF
 * @since 2021-05-24
 */
public interface TEnumFieldMapper extends BaseMapper<TEnumField> {


    @Select("<script>" +
            "SELECT TABLE_NAME name," +
            "       TABLE_COMMENT description " +
            "FROM information_schema.TABLES " +
            "WHERE table_schema='got_tickets_test' " +
            "<if test='tableName != null'>" +
            "   AND table_name = #{tableName}" +
            "</if>" +
            "</script>")
    List<TEnumField> listEnumTables(@Param("tableName") String tableName);


    @Select("SELECT COLUMN_NAME `name`, " +
            "       column_comment `comment`, " +
            "       column_type `type` " +
            "FROM INFORMATION_SCHEMA.Columns " +
            "WHERE table_schema='got_tickets_test'" +
            " AND table_name= #{tableName}")
    List<Map<String, String>> listEnumFields(@Param("tableName") String tableName);


    @Update("TRUNCATE ${tableName}")
    int truncateTable(@Param("tableName") String tableName);

    @Update("alter table ${tableName} " +
            "modify " +
            "column ${columnName} tinyint(4) comment '${comment}'")
    int updateColumnComment(
            @Param("tableName") String tableName,
            @Param("columnName") String columnName,
            @Param("comment") String comment
    );

}
