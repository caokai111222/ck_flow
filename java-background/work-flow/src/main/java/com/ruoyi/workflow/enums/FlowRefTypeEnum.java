package com.ruoyi.workflow.enums;

/**
 * 节点关联审批人类型枚举类
 *
 * @author ruoyi
 */
public enum FlowRefTypeEnum {

    /**
     * 指定人
     */
    REF_USER("1"),
    /**
     * 指定角色
     */
    REF_ROLE("2"),
    /**
     * 指定部门的指定角色
     */
    REF_DEPT_ROLE("3"),
    /**
     * 业务传入
     */
    INTPUT_USER("4");

    private final String info;

    FlowRefTypeEnum(String info) {
        this.info = info;
    }


    public String getInfo() {
        return info;
    }

}
