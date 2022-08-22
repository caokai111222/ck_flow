package com.ruoyi.workflow.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 流程模板主表
 *
 * @author ck
 */
@Data
@TableName(value = "flow_set_main")
public class FlowSetMain implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * id
     */
    @TableId(type = IdType.ASSIGN_ID)
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
     * 版本号
     */
    private Integer nowEdition;
    /**
     * 部门
     */
    private Long deptId;
    /**
     * 删除标志（0代表存在1代表删除）
     */
    private String delFlag;
    /**
     * 创建者
     */
    private String createBy;
    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    /**
     * 更新者
     */
    private String updateBy;
    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /**
     * 模板列表
     */
    @TableField(exist = false)
    private List<FlowSetModule> moduleList;
    /**
     * 走向列表
     */
    @TableField(exist = false)
    private List<FlowSetCondition> conditionList;

    /**
     * 快速通过节点和条件寻找下一路径的map
     */
    @TableField(exist = false)
    private Map<String, List<FlowSetCondition>> module2ConditionMap;
    /**
     * 快速通过路径寻找上下节点的map
     */
    @TableField(exist = false)
    private Map<String, FlowSetModule> condition2ModuleMap;
    /**
     * 开始节点
     */
    @TableField(exist = false)
    private FlowSetModule startNode;
    /**
     * 结束节点
     */
    @TableField(exist = false)
    private FlowSetModule endNode;
    /**
     * 用户id
     */
    @TableField(exist = false)
    private Long userId;
}
