package com.ruoyi.workflow.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.workflow.domain.FlowSetModule;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface FlowSetModuleMapper extends BaseMapper<FlowSetModule> {
    /**
     * 更新节点为非当前
     *
     * @param id
     * @param notNowFlag
     */
    @Update({"<script> update flow_set_module set current_flag = #{notNowFlag} where flow_set_main_id = #{id} </script>"})
    void updateForNotNow(@Param("id") Long id, @Param("notNowFlag") String notNowFlag);

    /**
     * 批量插入
     *
     * @param inserts
     */
    @Insert({"<script>insert into flow_set_module" +
            "(id," +
            "type," +
            "flow_set_main_id," +
            "module_name," +
            "module_code," +
            "role_ids," +
            "user_ids," +
            "apply_type," +
            "apply_scale," +
            "ref_type," +
            "current_flag," +
            "edition," +
            "coordinate_x," +
            "coordinate_y," +
            "auto_pass_type," +
            "delay_time," +
            "delay_time_unit," +
            "next_dept_type," +
            "next_dept_ids," +
            "form_main_id," +
            "form_main_name," +
            "flow_in_type)" +
            " values " +
            " <foreach collection=\"inserts\" item=\"item\" index=\"index\" open=\"\" close=\"\" separator=\" , \">" +
            "(#{item.id}," +
            "#{item.type}," +
            "#{item.flowSetMainId}," +
            "#{item.moduleName}," +
            "#{item.moduleCode}," +
            "#{item.roleIds}," +
            "#{item.userIds}," +
            "#{item.applyType}," +
            "#{item.applyScale}," +
            "#{item.refType}," +
            "#{item.currentFlag}," +
            "#{item.edition}," +
            "#{item.coordinateX}," +
            "#{item.coordinateY}," +
            "#{item.autoPassType}," +
            "#{item.delayTime}," +
            "#{item.delayTimeUnit}," +
            "#{item.nextDeptType}," +
            "#{item.nextDeptIds}," +
            "#{item.formMainId}," +
            "#{item.formMainName}," +
            "#{item.flowInType})" +
            "</foreach>" +
            "</script>"})
    void insertBatch(@Param("inserts") List<FlowSetModule> inserts);
}
