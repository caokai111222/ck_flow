package com.ruoyi.workflow.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 流程节点表
 *
 * @author ck
 */
@Data
@TableName(value = "flow_nodes")
public class FlowNodes implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    /**
     * 流程主表id
     */
    private Long orderId;
    /**
     * 模板id
     */
    private Long flowModuleId;
    /**
     * 上一节点id
     */
    private Long lastNodeId;
    /**
     * 业务key
     */
    private String businessKey;
    /**
     * 业务id
     */
    private Long businessId;
    /**
     * 节点状态
     */
    private String nodeStatus;
    /**
     * 审核结果
     */
    private String nodeRes;
    /**
     * 流程节点顺序
     */
    private Integer applySort;


    /**
     * 删除标志（0代表存在1代表删除）
     */
    private String delFlag;
    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
}
