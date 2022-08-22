package com.ruoyi.workflow.enums;

/**
 * 流程业务异常枚举
 *
 * @author ruoyi
 */
public enum FlowExceptionEnum {
    NOT_HAS_MAIN_KEY(0, "没有这个流程"),
    NOT_HAS_MODULE_KEY(1, "没有这个流程节点"),
    NOT_HAS_ORDER(2, "没有这个流程工单或者工单已经结单/关闭"),
    ERROR_MODULE_ID(3, "流程节点错误，请刷新后重试"),
    ERROR_NODE_STATUS(4, "流程节点不在等待处理状态，请刷新后重试"),
    NOT_HAS_TASK(5, "没有这个流程任务"),
    NOT_NEXT_CONDITIONS(6, "没有对应操作结果路径"),
    NOT_NEXT_USERS(7, "需要传入下一节点审批人"),
    HAS_SAME_IN_FLOW(8, "该业务有正在进行中的工单，请不要重复提交申请");

    private final Integer code;
    private final String info;

    FlowExceptionEnum(Integer code, String info) {
        this.code = code;
        this.info = info;
    }

    public Integer getCode() {
        return code;
    }

    public String getInfo() {
        return info;
    }

    /**
     * 根据code获取info
     *
     * @param code
     * @return
     */
    public static String getInfo(Integer code) {
        FlowExceptionEnum[] enums = values();
        for (FlowExceptionEnum flowExceptionEnum : enums) {
            if (flowExceptionEnum.getCode().equals(code)) {
                return flowExceptionEnum.getInfo();
            }
        }
        return null;
    }

    /**
     * 根据info获取code
     *
     * @param info
     * @return
     */
    public static Integer getCode(String info) {
        FlowExceptionEnum[] enums = values();
        for (FlowExceptionEnum flowExceptionEnum : enums) {
            if (flowExceptionEnum.getInfo().equals(info)) {
                return flowExceptionEnum.getCode();
            }
        }
        return null;
    }

}
