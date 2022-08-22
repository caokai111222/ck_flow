package com.ruoyi.workflow.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.pagehelper.PageInfo;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.workflow.domain.FlowSetMain;
import com.ruoyi.workflow.domain.dto.FlowSetDto;
import com.ruoyi.workflow.exception.FlowException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.workflow.service.IFlowSetMainService;

import java.util.List;

import static com.ruoyi.common.utils.PageUtils.startPage;

/**
 * 流程配置Conroller
 */
@RestController
@RequestMapping("/flowSet")
public class FlowSetMainConroller {
    @Autowired
    private IFlowSetMainService flowSetMainService;

    /**
     * 获取列表
     */
    @GetMapping("/list")
    public AjaxResult list(FlowSetMain flowSetMain) {
        startPage();
        List<FlowSetMain> list = flowSetMainService.list(new LambdaQueryWrapper<FlowSetMain>()
                .eq(FlowSetMain::getDelFlag, Constants.NOT_DEL)
                .like(StringUtils.isNotEmpty(flowSetMain.getFlowName()), FlowSetMain::getFlowName, flowSetMain.getFlowName())
                .like(StringUtils.isNotEmpty(flowSetMain.getFlowKey()), FlowSetMain::getFlowKey, flowSetMain.getFlowKey())
                .orderByDesc(FlowSetMain::getCreateTime)
        );
        return AjaxResult.success(new PageInfo<FlowSetMain>(list));
    }

    /**
     * 根据编号获取详细信息
     */
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable Long id) {
        return AjaxResult.success(flowSetMainService.selectInfoById(id));
    }

    /**
     * 新增
     */
    @Log(title = "流程管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody FlowSetDto flowSetDto) {
        try {
            flowSetMainService.saveFlowSet(flowSetDto);
        } catch (FlowException e) {
            throw e;
        }
        return AjaxResult.success();
    }

    /**
     * 编辑
     */
    @Log(title = "流程管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody FlowSetDto flowSetDto) {
        try {
            flowSetMainService.saveFlowSet(flowSetDto);
        } catch (FlowException e) {
            throw e;
        }
        return AjaxResult.success();
    }
}
