package com.ruoyi.workflow.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 流程表单记录表
 *
 * @author ck
 */
@Data
@TableName(value = "flow_form")
public class FlowForm implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    private Long orderId;
    private Long moduleId;
    private Long nodeId;
    private Long taskId;
    private Long formId;
    private Long formItemId;
    private Long userId;
    private String itemName;
    private String itemValue;
    /**
     * 删除标志（0代表存在1代表删除）
     */
    private String delFlag;
    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
}
