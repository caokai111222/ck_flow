package com.ruoyi.workflow.constant;

import java.math.BigDecimal;

/**
 * 流程常量信息
 *
 * @author ruoyi
 */
public class FlowConstant {

    /**
     * 默认通过审批的Y
     */
    public static final String NORMAL_PASS = "Y";
    /**
     * 流程审批结果，没能匹配
     */
    public static final String NOT_MATCHED = "N";
    /**
     * 当前
     */
    public static final String NOW_FLAG = "Y";
    /**
     * 当前
     */
    public static final String NOT_NOW_FLAG = "N";

    /**
     * 关闭或结束
     */
    public static final String STATUS_CLOSE_OR_END = "Y";

    /**
     * 正常
     */
    public static final String STATUS_NORMAL = "N";
    /**
     * 用于计算百分比的100
     */
    public final static BigDecimal LONG_100 = BigDecimal.valueOf(100);

    /**
     * 流程主表redis cache key
     */
    public static final String FLOW_MAIN_ID = "flow_main_id:";
    /**
     * 流程主表redis cache key
     */
    public static final String FLOW_MAIN_KEY = "flow_main_key:";
    /**
     * 流程管理redis总key
     */
    public static final String FLOW_MAP_KEY = "flow_engine";
    /**
     * 流程进行redlock前缀
     */
    public static final String FLOW_LOCK_NEXT_KEY = "flow_next:";
    /**
     * 流程创建redlock前缀
     */
    public static final String FLOW_LOCK_ADD_KEY = "flow_add:";

    /**
     * 自定义表单redis总key
     */
    public static final String FORM_MAP_KEY = "form_main:";
    /**
     * 自定义表单redis cache key
     */
    public static final String FORM_MAIN_ID = "form_main_id:";
}
