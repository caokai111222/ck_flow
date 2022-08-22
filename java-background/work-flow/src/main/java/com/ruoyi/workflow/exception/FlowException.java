package com.ruoyi.workflow.exception;

import com.ruoyi.workflow.enums.FlowExceptionEnum;
import lombok.Data;

@Data
public final class FlowException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    /**
     * 错误码
     */
    private Integer code;

    /**
     * 错误提示
     */
    private String message;

    /**
     * 空构造方法，避免反序列化问题
     */
    public FlowException() {
    }

    public FlowException(Integer code) {
        this.code = code;
        this.message = FlowExceptionEnum.getInfo(code);
    }

    public FlowException(String message) {
        this.code = FlowExceptionEnum.getCode(message);
        this.message = message;
    }

    public FlowException(String message, Integer code) {
        this.message = message;
        this.code = code;
    }

    public FlowException(FlowExceptionEnum notHasMainKey) {
        this.message = notHasMainKey.getInfo();
        this.code = notHasMainKey.getCode();
    }
}
