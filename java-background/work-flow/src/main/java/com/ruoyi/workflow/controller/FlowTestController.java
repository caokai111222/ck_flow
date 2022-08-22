package com.ruoyi.workflow.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.pagehelper.PageInfo;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.workflow.domain.FlowSetMain;
import com.ruoyi.workflow.domain.dto.FlowInfoDto;
import com.ruoyi.workflow.domain.dto.FlowSetDto;
import com.ruoyi.workflow.domain.vo.FlowMyTodoDto;
import com.ruoyi.workflow.exception.FlowException;
import com.ruoyi.workflow.service.IFlowService;
import com.ruoyi.workflow.service.IFlowSetMainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.ruoyi.common.utils.PageUtils.startPage;

/**
 * 流程配置Conroller
 */
@RestController
@RequestMapping("/flow")
public class FlowTestController {
    @Autowired
    private IFlowService flowService;
    /**
     * 获取列表
     */
    @GetMapping("/myTodo")
    public AjaxResult list(FlowSetMain flowSetMain) {
        startPage();
        return AjaxResult.success(new PageInfo<FlowMyTodoDto>(flowService.getMyTodo(flowSetMain)));
    }
    /**
     * 新增
     */
    @PostMapping("/add")
    public AjaxResult add(@Validated @RequestBody FlowInfoDto flowInfo) {
        try {
            flowService.startNewFlow(flowInfo);
        } catch (FlowException e) {
            throw e;
        }
        return AjaxResult.success();
    }

    /**
     * 新增
     */
    @PostMapping("/flowToNext")
    public AjaxResult flowToNext(@Validated @RequestBody FlowInfoDto flowInfo) {
        try {
            flowService.flowToNext(flowInfo);
        } catch (FlowException e) {
            throw e;
        }
        return AjaxResult.success();
    }
    /**
     * 审批记录
     */
    @GetMapping("/flowRecord")
    public AjaxResult flowRecord(FlowInfoDto flowInfo) {
        startPage();
        return AjaxResult.success(flowService.queryFlowRecordByOrderId(flowInfo));
    }
    /**
     * 审批记录
     */
    @GetMapping("/getFormDataById")
    public AjaxResult getFormDataById(FlowInfoDto flowInfo) {
        return AjaxResult.success(flowService.getFormDataById(flowInfo));
    }
}
