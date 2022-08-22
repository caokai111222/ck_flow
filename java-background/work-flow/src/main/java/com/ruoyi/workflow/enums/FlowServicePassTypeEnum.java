package com.ruoyi.workflow.enums;

/**
 * 系统任务自动通过类型枚举类
 *
 * @author ruoyi
 */
public enum FlowServicePassTypeEnum {

    /**
     * 立即通过
     */
    PASS_NOW("1"),
    /**
     * 延迟通过
     */
    PASS_DELAY("2");

    private final String info;

    FlowServicePassTypeEnum(String info) {
        this.info = info;
    }


    public String getInfo() {
        return info;
    }

}
