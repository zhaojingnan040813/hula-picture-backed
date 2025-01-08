package com.hula.common;

import com.hula.exception.ErrorCode;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author: 赵景南
 * @Date: 2025/1/7 22:20
 * @Version: v1.0.0
 * @Description: 略
 **/
@Data
public class BaseResponse<T> implements Serializable {

    private int code;

    private T data;

    private String message;

    public BaseResponse(int code, T data, String message) {
        this.code = code;
        this.data = data;
        this.message = message;
    }

    public BaseResponse(int code, T data) {
        this(code, data, "");
    }

    public BaseResponse(ErrorCode errorCode) {
        this(errorCode.getCode(), null, errorCode.getMessage());
    }
}

