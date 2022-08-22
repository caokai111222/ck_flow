package com.ruoyi.workflow.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.workflow.domain.FlowForm;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface FlowFormMapper extends BaseMapper<FlowForm> {
    /**
     * 批量插入
     *
     * @param inserts
     */
    @Insert({"<script>insert into flow_form" +
            "(id," +
            "order_id," +
            "module_id," +
            "node_id," +
            "task_id," +
            "form_id," +
            "form_item_id," +
            "user_id," +
            "item_name," +
            "item_value," +
            "del_flag," +
            "create_time)" +
            " values " +
            " <foreach collection=\"inserts\" item=\"item\" index=\"index\" open=\"\" close=\"\" separator=\" , \">" +
            "(#{item.id}," +
            "#{item.orderId}," +
            "#{item.moduleId}," +
            "#{item.nodeId}," +
            "#{item.taskId}," +
            "#{item.formId}," +
            "#{item.formItemId}," +
            "#{item.userId}," +
            "#{item.itemName}," +
            "#{item.itemValue}," +
            "#{item.delFlag}," +
            "#{item.createTime})" +
            "</foreach>" +
            "</script>"})
    void insertBatch(@Param("inserts") List<FlowForm> inserts);
}
