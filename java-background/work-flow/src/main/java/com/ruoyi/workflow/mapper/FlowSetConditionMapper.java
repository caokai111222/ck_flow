package com.ruoyi.workflow.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.workflow.domain.FlowSetCondition;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface FlowSetConditionMapper extends BaseMapper<FlowSetCondition> {
    /**
     * 批量插入
     *
     * @param inserts
     */
    @Insert({"<script>insert into flow_set_condition" +
            "(id," +
            "edition," +
            "flow_set_main_id," +
            "flow_module_id," +
            "next_flow_module_id," +
            "condition_key," +
            "condition_type," +
            "remark)" +
            " values " +
            " <foreach collection=\"inserts\" item=\"item\" index=\"index\" open=\"\" close=\"\" separator=\" , \">" +
            "(#{item.id}," +
            "#{item.edition}," +
            "#{item.flowSetMainId}," +
            "#{item.flowModuleId}," +
            "#{item.nextFlowModuleId}," +
            "#{item.conditionKey}," +
            "#{item.conditionType}," +
            "#{item.remark})" +
            "</foreach>" +
            "</script>"})
    void insertBatch(@Param("inserts") List<FlowSetCondition> inserts);
}
