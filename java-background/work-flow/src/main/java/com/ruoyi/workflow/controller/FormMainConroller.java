package com.ruoyi.workflow.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.pagehelper.PageInfo;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.workflow.constant.FlowConstant;
import com.ruoyi.workflow.domain.FormMain;
import com.ruoyi.workflow.domain.dto.FormMainDto;
import com.ruoyi.workflow.exception.FlowException;
import com.ruoyi.workflow.service.IFormMainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.ruoyi.common.utils.PageUtils.startPage;

/**
 * 表单配置Conroller
 */
@RestController
@RequestMapping("/formMain")
public class FormMainConroller {
    @Autowired
    private IFormMainService formMainService;

    /**
     * 获取列表
     */
    @GetMapping("/list")
    public AjaxResult list(FormMain formMain) {
        startPage();
        List<FormMain> list = formMainService.list(new LambdaQueryWrapper<FormMain>()
                .eq(FormMain::getDelFlag, Constants.NOT_DEL)
                .eq(FormMain::getNowFlag, FlowConstant.NOW_FLAG)
                .like(StringUtils.isNotEmpty(formMain.getFormCode()), FormMain::getFormCode, formMain.getFormCode())
                .like(StringUtils.isNotEmpty(formMain.getFormName()), FormMain::getFormName, formMain.getFormName())
                .orderByDesc(FormMain::getCreateTime)
        );
        return AjaxResult.success(new PageInfo<FormMain>(list));
    }

    /**
     * 根据编号获取详细信息
     */
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable Long id) {
        return AjaxResult.success(formMainService.selectInfoById(id));
    }

    /**
     * 新增
     */
    @Log(title = "表单管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody FormMainDto formMainDto) {
        try {
            formMainService.saveFormMain(formMainDto);
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
    public AjaxResult edit(@Validated @RequestBody FormMainDto formMainDto) {
        try {
            formMainService.saveFormMain(formMainDto);
        } catch (FlowException e) {
            throw e;
        }
        return AjaxResult.success();
    }
}
