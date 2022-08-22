package com.ruoyi.workflow.enums;

/**
 * 节点状态枚举类
 *
 * @author ruoyi
 */
public enum FlowNodeStatusEnum {
    /**
     * 等待审批中
     */
    WAITING("waiting"),
    /**
     * 审批操作完毕
     */
    PASS("pass"),
    /**
     * auto审核完毕
     */
    AUTO_PASS("auto_pass"),
    /**
     * 节点通过
     */
    DONE("done");

    private final String info;

    FlowNodeStatusEnum(String info) {
        this.info = info;
    }


    public String getInfo() {
        return info;
    }

}
