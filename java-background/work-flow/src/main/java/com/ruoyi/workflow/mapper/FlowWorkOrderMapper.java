package com.ruoyi.workflow.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.workflow.domain.FlowWorkOrder;
import org.apache.ibatis.annotations.Param;

public interface FlowWorkOrderMapper extends BaseMapper<FlowWorkOrder> {
    /**
     * 宽表当前节点，待办人等数据更新
     *
     * @param orderId
     */
    void updateForOrderInfo(@Param("orderId") Long orderId);
}
