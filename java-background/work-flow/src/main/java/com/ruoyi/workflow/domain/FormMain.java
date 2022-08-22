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

/**
 * 自定义表单主表
 *
 * @author ck
 */
@Data
@TableName(value = "form_main")
public class FormMain implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 表单名
     */
    private String formName;

    /**
     * 表单code
     */
    private String formCode;

    /**
     * 表单描述
     */
    private String formDesc;

    /**
     * 禁用表单
     */
    private String disableFlag;

    /**
     * 部门
     */
    private Long deptId;
    /**
     * 版本号
     */
    private Integer edition;
    /**
     * 当前版本
     */
    private String nowFlag;

    /**
     * 是否系统
     */
    private String sysFlag;
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
     * 修改时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
    /**
     * 更新者
     */
    private String updateBy;

    /**
     * 按照层级分级的控件列表
     */
    @TableField(exist = false)
    private List<FormItem> itemList;
    /**
     * 不按照层级分级的控件列表
     */
    @TableField(exist = false)
    private List<FormItem> items;
}
