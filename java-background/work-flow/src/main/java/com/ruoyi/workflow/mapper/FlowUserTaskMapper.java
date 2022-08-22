package com.ruoyi.workflow.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.workflow.domain.FlowSetMain;
import com.ruoyi.workflow.domain.FlowUserTask;
import com.ruoyi.workflow.domain.FlowWorkOrder;
import com.ruoyi.workflow.domain.dto.FlowInfoDto;
import com.ruoyi.workflow.domain.dto.FlowNodeTaskDto;
import com.ruoyi.workflow.domain.vo.FlowMyTodoDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface FlowUserTaskMapper extends BaseMapper<FlowUserTask> {
    /**
     * 批量保存任务
     *
     * @param inserts
     */
    void insertBatch(@Param("inserts") List<FlowUserTask> inserts);

    /**
     * 等待操作的全部任务变成已经操作
     *
     * @param id
     * @param status
     * @param oldStatus
     */
    void clearForInWait(@Param("id") Long id, @Param("status") String status, @Param("oldStatus") String oldStatus);

    /**
     * 获取统计
     *
     * @param nodeId
     * @param applyRes
     * @return
     */
    FlowNodeTaskDto selectForCount(@Param("nodeId") Long nodeId, @Param("applyRes") String applyRes);

    /**
     * 等待中的都标为已办(整个工单)
     *
     * @param status
     * @param oldStatus
     * @param order
     */
    void clearForInWaitAll(@Param("status") String status, @Param("oldStatus") String oldStatus, @Param("order") FlowWorkOrder order);

    /**
     * 查询指定节点的用户
     *
     * @param flowModuleId
     * @param orderId
     * @return
     */
    FlowUserTask selectByModuleId(@Param("flowModuleId") Long flowModuleId, @Param("orderId") Long orderId);

    /**
     * 查询我的任务
     *
     * @param flowInfo
     * @return
     */
    FlowUserTask selectByInfo(@Param("flowInfo") FlowInfoDto flowInfo);

    /**
     * 获取我的待办
     *
     * @param flowSetMain
     * @return
     */
    List<FlowMyTodoDto> selectMyTodo(FlowSetMain flowSetMain);

    /**查询审批记录
     *
     * @param flowInfo
     * @return
     */
    List<FlowMyTodoDto> selectFlowRecordByOrderId(FlowInfoDto flowInfo);
}
