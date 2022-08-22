package com.ruoyi.workflow.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.workflow.domain.FlowSetModule;
import com.ruoyi.workflow.domain.FlowWorkOrder;
import com.ruoyi.workflow.domain.dto.FlowInfoDto;
import com.ruoyi.workflow.domain.FlowNodes;
import com.ruoyi.workflow.domain.dto.FlowNodeTaskDto;
import com.ruoyi.workflow.domain.vo.FlowNodesPercent;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface FlowNodesMapper extends BaseMapper<FlowNodes> {

    /**
     * 批量保存
     *
     * @param inserts
     */
    void insertBatch(@Param("inserts") List<FlowNodes> inserts);

    /**
     * 获取各个操作结果的数量
     *
     * @param flowInfo
     * @return
     */
    List<FlowNodesPercent> selectForPercent(@Param("flowInfo") FlowInfoDto flowInfo);

    /**
     * 标记为已办
     *
     * @param order
     * @param module
     */
    void updateForDid(@Param("order") FlowWorkOrder order, @Param("module") FlowSetModule module);

    /**
     * 等待中的都标为已办
     *
     * @param flowSetModule
     * @param status
     * @param order
     */
    void clearForInWait(@Param("flowSetModule") FlowSetModule flowSetModule, @Param("status") String status, @Param("order") FlowWorkOrder order);

    /**
     * 获取路径节点统计
     *
     * @param id
     * @param orderId
     * @return
     */
    FlowNodeTaskDto selectForCount(@Param("id") Long id, @Param("orderId") Long orderId);

    /**
     * 等待中的都标为已办(整个工单)
     *
     * @param status
     * @param oldStatus
     * @param order
     */
    void clearForInWaitAll(@Param("status") String status, @Param("oldStatus") String oldStatus, @Param("order") FlowWorkOrder order);

    /**
     * 查找进行/等待中的节点
     *
     * @param orderId
     * @param moduleIds
     * @return
     */
    List<FlowNodes> selectByOrderIdAndModuleIds(@Param("orderId") Long orderId, @Param("moduleIds") String moduleIds);
}
