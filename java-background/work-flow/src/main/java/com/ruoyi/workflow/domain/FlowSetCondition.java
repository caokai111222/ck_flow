package com.ruoyi.workflow.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 流程条件表
 *
 * @author ck
 */
@Data
@TableName(value = "flow_set_condition")
public class FlowSetCondition implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    /**
     * 主表id
     */
    private Long flowSetMainId;
    /**
     * 版本号
     */
    private Integer edition;
    /**
     * 节点id
     */
    private Long flowModuleId;
    /**
     * 下一节点id
     */
    private Long nextFlowModuleId;
    /**
     * 1等于2不等于3包含列表
     */
    private String conditionType;
    /**
     * 触发条件key
     */
    private String conditionKey;
    /**
     * 备注
     */
    private String remark;
}
