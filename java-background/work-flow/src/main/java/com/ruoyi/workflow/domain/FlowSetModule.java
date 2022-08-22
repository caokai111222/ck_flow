package com.ruoyi.workflow.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.core.domain.entity.SysUser;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 流程模板模板表
 *
 * @author ck
 */
@Data
@TableName(value = "flow_set_module")
public class FlowSetModule implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    /**
     * 类型
     */
    private String type;
    /**
     * 主表id
     */
    private Long flowSetMainId;
    /**
     * 模板名称
     */
    private String moduleName;
    /**
     * 模板编码
     */
    private String moduleCode;
    /**
     * 指定角色id
     */
    private String roleIds;
    /**
     * 指定审批人id
     */
    private String userIds;
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
     * 是否当前版本（新开的流程走当前）
     */
    private String currentFlag;

    /**
     * 版本号
     */
    private Integer edition;
    /**
     * x轴坐标
     */
    private Integer coordinateX;
    /**
     * y轴坐标
     */
    private Integer coordinateY;

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
     * 下一节点部门类型 1本部门2父级3指定部门4业务传入的部门
     */
    private String nextDeptType;
    /**
     * 指定部门
     */
    private String nextDeptIds;
    /**
     * 指定部门类型
     */
    private String deptType;
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
     * 表单名称
     */
    private String formMainName;
    /**
     * 表单id
     */
    private Long formMainId;

    /**
     * 指定审批人列表
     */
    @TableField(exist = false)
    private List<SysUser> userList;
}
