package com.ruoyi.workflow.domain;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 业务工单表
 *
 * @author ck
 */
@Data
@TableName(value = "flow_work_order")
public class FlowWorkOrder implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    /**
     * 业务key
     */
    private String businessKey;
    /**
     * 业务id
     */
    private Long businessId;
    /**
     * 流程id
     */
    private Long flowMainId;

    /**
     * 工单编号
     */
    private String workFlowCode;
    /**
     * 是否已结单
     */
    private String endFlag;
    /**
     * 是否已关闭
     */
    private String closeFlag;
    /**
     * 当前节点id
     */
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String nowModuleIds;
    /**
     * 当前节点名称
     */
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String nowModuleNames;
    /**
     * 当前操作人id
     */
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String nowUserIds;
    /**
     * 当前操作人name
     */
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String nowUserNames;
    /**
     * 结束工单时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endDate;
    /**
     * 关闭工单时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date closeDate;
    /**
     * 当前待办操作人id
     */
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String todoUserIds;
    /**
     * 当前待办操作人name
     */
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String todoUserNames;

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
}
