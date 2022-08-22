package com.ruoyi.workflow.enums;

/**
 * 节点类型枚举类
 *
 * @author ruoyi
 */
public enum FlowNodeTypeEnum {
    START_NODE("bpmn:startEvent"),
    END_NODE("bpmn:endEvent"),
    USER_TASK("bpmn:userTask"),
    SERVICE_TASK("bpmn:serviceTask");

    private final String info;

    FlowNodeTypeEnum(String info) {
        this.info = info;
    }


    public String getInfo() {
        return info;
    }

}
