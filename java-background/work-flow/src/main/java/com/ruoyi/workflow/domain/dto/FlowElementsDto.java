package com.ruoyi.workflow.domain.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 流程元素对象
 */
@Data
public class FlowElementsDto implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * id
     */
    private String id;

    /**
     * type 类型
     */
    private String type;

    /**以下边
     *
     */


    /**
     * 源id
     */
    private String sourceNodeId;
    /**
     * 目标id
     */
    private String targetNodeId;
    /**
     * 备注
     */
    private String remark;
    /**
     * 1等于2不等于3包含列表
     */
    private String conditionType;
    /**
     * 触发条件key
     */
    private String conditionKey;


    /**以下节点
     *
     */

    /**
     * 模板名称
     */
    private String moduleName;
    /**
     * 1：1人通过即可，2：必须所有人通过，3：超过指定比例的人通过
     */
    private String applyType;
    /**
     * 0-100%,apply_type=3的时候有效，通过比例
     */
    private BigDecimal applyScale;
    /**
     * 1指定人2指定角色3指定部门的角色
     */
    private String refType;
    /**
     * 下一节点部门类型 1本部门2父级3指定部门4业务传入的部门
     */
    private String nextDeptType;
    /**
     * 指定部门
     */
    private String nextDeptIds;
    /**
     * 指定角色id
     */
    private List<Long> roleIds;
    /**
     * 指定审批人id
     */
    private String userIds;
    /**
     * 进入条件 1单一线路满足 2全部线路满足
     */
    private String flowInType;

    /**
     * 系统任务通过策略
     */
    private String autoPassType;
    /**
     * 系统任务延迟时间
     */
    private Integer delayTime;
    /**
     * 系统任务延迟时间单位
     */
    private String delayTimeUnit;
    /**
     * x轴坐标
     */
    private Integer coordinateX;
    /**
     * y轴坐标
     */
    private Integer coordinateY;
    /**
     * 表单名称
     */
    private String formMainName;
    /**
     * 表单id
     */
    private Long formMainId;
}
