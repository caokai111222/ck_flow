package com.ruoyi.workflow.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 自定义表单控件表
 *
 * @author ck
 */
@Data
@TableName(value = "form_item")
public class FormItem implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    /**
     * 主表id
     */
    private Long formId;

    /**
     * 容器id
     */
    private Long pid;
    /**
     * 类型
     */
    private String itemIcon;
    /**
     * 类型
     */
    private String itemTag;
    /**
     * 类型
     */
    private String itemType;

    /**
     * name
     */
    private String itemName;

    /**
     * 配置项
     */
    private String configJson;
    /**
     * 配置项
     */
    private Integer itemSort;
    /**
     * 删除位
     */
    private String delFlag;

    @TableField(exist = false)
    private List<FormItem> children;
    /**
     * key
     */
    @TableField(exist = false)
    private String key;
    /**
     * pkey
     */
    @TableField(exist = false)
    private String pkey;
}
