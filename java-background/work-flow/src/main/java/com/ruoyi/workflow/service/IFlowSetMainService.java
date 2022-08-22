package com.ruoyi.workflow.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.workflow.domain.FlowSetMain;
import com.ruoyi.workflow.domain.dto.FlowSetDto;

/**
 * 流程配置表service
 */
public interface IFlowSetMainService extends IService<FlowSetMain> {
    /**
     * 获取详情
     *
     * @param id
     * @return
     */
    FlowSetMain selectInfoById(Long id);

    /**
     * 根据key获取详情
     *
     * @param key
     * @return
     */
    FlowSetMain selectInfoByKey(String key);

    /**
     * 保存新的流程模板
     *
     * @param flowSetDto
     */
    void saveFlowSet(FlowSetDto flowSetDto);
}
