package com.ruoyi.workflow.enums;

/**
 * 节点下一部门类型枚举类
 *
 * @author ruoyi
 */
public enum FlowDeptTypeEnum {

    /**
     * 发起人部门
     */
    THIS_DEPT("1"),
    /**
     * 发起人上级部门
     */
    PARENT_DEPT("2"),
    /**
     * 指定部门
     */
    SPEC_DEPT("3"),
    /**
     * 业务传入
     */
    INPUT_DEPT("4"),
    /**
     * 指定部门类型
     */
    SEPC_DEPT_TYPE("5"),
    /**
     * 未知
     */
    OTHER("OTHER");

    private final String info;

    FlowDeptTypeEnum(String info) {
        this.info = info;
    }


    public String getInfo() {
        return info;
    }

    /**
     * 根据info反推枚举类
     *
     * @param info
     * @return
     */
    public static FlowDeptTypeEnum getByInfo(String info) {
        for (FlowDeptTypeEnum type : values()) {
            if (type.getInfo().equals(info)) {
                return type;
            }
        }
        return OTHER;
    }
}
