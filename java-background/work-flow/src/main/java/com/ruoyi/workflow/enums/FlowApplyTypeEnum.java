package com.ruoyi.workflow.enums;

/**
 * 节点通过类型枚举类
 *
 * @author ruoyi
 */
public enum FlowApplyTypeEnum {

    /**
     * 流程审批类型，1人通过即可
     */
    APPLY_SINGLE("1"),
    /**
     * 流程审批类型，必须所有人通过
     */
    APPLY_ALL("2"),
    /**
     * 流程审批类型，超过指定比例的人通过
     */
    APPLY_SCLAE("3");

    private final String info;

    FlowApplyTypeEnum(String info) {
        this.info = info;
    }


    public String getInfo() {
        return info;
    }

}
