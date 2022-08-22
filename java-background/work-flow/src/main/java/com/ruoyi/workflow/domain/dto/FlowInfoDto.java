package com.ruoyi.workflow.domain.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 流程传递对象
 */
@Data
public class FlowInfoDto implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 业务id
     */
    private Long businessId;
    /**
     * 业务id
     */
    private String businessKey;
    /**
     * 流程key
     */
    private String flowMainKey;
    /**
     * 流程模板key
     */
    private String flowModuleKey;
    /**
     * 流程模板id
     */
    private Long flowModuleId;
    /**
     * 流程节点id
     */
    private Long nodeId;
    /**
     * 流程任务id
     */
    private Long taskId;
    /**
     * 工单id
     */
    private Long orderId;
    /**
     * 部门iD
     */
    private Long deptId;
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 用户ids
     */
    private String userIds;

    /**
     * 流程模板key
     */
    private String applyRes;
    /**
     * 审核备注
     */
    private String applyRemark;
    /**
     * 附件
     */
    private String filePaths;
    /**
     * 自动提交
     */
    private String autoCommit;
    /**
     * 自动提交
     */
    private String dataJson;
}