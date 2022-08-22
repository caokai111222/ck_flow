package com.ruoyi.workflow.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.workflow.domain.FormItem;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface FormItemMapper extends BaseMapper<FormItem> {
    /**
     * 批量插入
     *
     * @param inserts
     */
    @Insert({"<script>insert into form_item" +
            "(id," +
            "form_id," +
            "pid," +
            "item_type," +
            "item_name," +
            "config_json," +
            "item_sort," +
            "del_flag)" +
            " values " +
            " <foreach collection=\"inserts\" item=\"item\" index=\"index\" open=\"\" close=\"\" separator=\" , \">" +
            "(#{item.id}," +
            "#{item.formId}," +
            "#{item.pid}," +
            "#{item.itemType}," +
            "#{item.itemName}," +
            "#{item.configJson}," +
            "#{item.itemSort}," +
            "'0')" +
            "</foreach>" +
            "</script>"})
    void insertBatch(@Param("inserts") List<FormItem> inserts);
}
