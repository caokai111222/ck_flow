package com.ruoyi.workflow.enums;

/**
 * 系统任务自动通过延迟时间类型枚举类
 *
 * @author ruoyi
 */
public enum FlowServiceDelayTimeUnitEnum {

    /**
     * 分钟
     */
    MINUTE("1"),
    /**
     * 小时
     */
    HOUR("2"),
    /**
     * 天
     */
    DAY("3"),
    /**
     * 未知
     */
    OTHER("0");

    private final String info;

    FlowServiceDelayTimeUnitEnum(String info) {
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
    public static FlowServiceDelayTimeUnitEnum getByInfo(String info) {
        for (FlowServiceDelayTimeUnitEnum type : values()) {
            if (type.getInfo().equals(info)) {
                return type;
            }
        }
        return OTHER;
    }
}
