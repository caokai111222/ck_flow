package com.ruoyi.workflow.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.workflow.domain.FlowSetMain;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface FlowSetMainMapper extends BaseMapper<FlowSetMain> {
    @Select({"select id," +
            "flow_name," +
            "flow_key," +
            "now_edition from flow_set_main where del_flag = '" + Constants.NOT_DEL + "' " +
            "and flow_key = #{flowMainKey}"})
    FlowSetMain selectByKey(@Param("flowMainKey") String flowMainKey);
}
