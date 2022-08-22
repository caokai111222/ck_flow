package com.ruoyi.workflow.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.workflow.domain.FlowForm;
import com.ruoyi.workflow.domain.FlowNodes;
import com.ruoyi.workflow.domain.FlowSetMain;
import com.ruoyi.workflow.domain.dto.FlowInfoDto;
import com.ruoyi.workflow.domain.vo.FlowMyTodoDto;

import java.util.List;

/**
 * 流程实际执行service
 */
public interface IFlowService extends IService<FlowNodes> {
    /**
     * 启动流程
     *
     * @param flowInfo 参数
     * @return 当前流程节点id
     */
    void startNewFlow(FlowInfoDto flowInfo);

    /**
     * 流程审批/驳回
     *
     * @param flowInfo 参数
     * @return 当前流程节点id
     */
    void flowToNext(FlowInfoDto flowInfo);

    /**
     * 关闭流程
     *
     * @param flowInfo 参数
     * @return 是否关闭成功
     */
    Boolean closeFlow(FlowInfoDto flowInfo);

    /**
     * 获取我的待办
     *
     * @param flowSetMain
     * @return
     */
    List<FlowMyTodoDto> getMyTodo(FlowSetMain flowSetMain);

    /**
     * 根据工单号查询审批记录
     *
     * @param flowInfo
     * @return
     */
    PageInfo queryFlowRecordByOrderId(FlowInfoDto flowInfo);

    /**
     * 根据taskid获取表单数据
     *
     * @param flowInfo
     * @return
     */
    List<FlowForm> getFormDataById(FlowInfoDto flowInfo);
}
