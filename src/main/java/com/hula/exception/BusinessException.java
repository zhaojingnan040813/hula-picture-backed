package com.hula.exception;

import lombok.Getter;

/**
 * @Author: 赵景南
 * @Date: 2025/1/7 22:22
 * @Version: v1.0.0
 * @Description: 略
 **/
@Getter
public class BusinessException extends RuntimeException {

    /**
     * 错误码
     */
    private final int code;

    public BusinessException(int code, String message) {
        super(message);
        this.code = code;
    }

    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
    }

    public BusinessException(ErrorCode errorCode, String message) {
        super(message);
        this.code = errorCode.getCode();
    }

}

