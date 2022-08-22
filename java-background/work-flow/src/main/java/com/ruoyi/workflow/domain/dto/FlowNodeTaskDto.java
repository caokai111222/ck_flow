package com.ruoyi.workflow.domain.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 任务/节点统计对象
 */
@Data
public class FlowNodeTaskDto implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 同一级 任务/节点总数
     */
    private BigDecimal allCount;
    /**
     * 同一级 任务/节点 相同结果数
     */
    private BigDecimal sameCount;

}
