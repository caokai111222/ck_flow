package com.ruoyi.workflow.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.workflow.domain.FormMain;
import com.ruoyi.workflow.domain.dto.FormMainDto;

/**
 * 表单配置表service
 */
public interface IFormMainService extends IService<FormMain> {
    /**
     * 根据id查询
     *
     * @param id
     * @return
     */
    FormMain selectInfoById(Long id);

    /**
     * 新增
     *
     * @param formMainDto
     */
    void saveFormMain(FormMainDto formMainDto);
}
