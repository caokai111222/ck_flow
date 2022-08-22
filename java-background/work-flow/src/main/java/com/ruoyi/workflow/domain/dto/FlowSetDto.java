package com.ruoyi.workflow.domain.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 流程设置对象
 */
@Data
public class FlowSetDto implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * id
     */
    private Long id;
    /**
     * 模板名称
     */
    private String flowName;
    /**
     * 模板key
     */
    private String flowKey;
    /**
     * 边
     */
    private List<FlowElementsDto> edges;
    /**
     * 节点
     */
    private List<FlowElementsDto> nodes;
}
