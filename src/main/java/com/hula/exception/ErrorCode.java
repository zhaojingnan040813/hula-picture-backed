package com.hula.exception;

import lombok.Getter;

/**
 * @Author: 赵景南
 * @Date: 2025/1/7 22:22
 * @Version: v1.0.0
 * @Description: 略
 **/
@Getter
public enum ErrorCode {

    SUCCESS(0, "ok"),
    PARAMS_ERROR(40000, "请求参数错误"),
    NOT_LOGIN_ERROR(40100, "未登录"),
    NO_AUTH_ERROR(40101, "无权限"),
    NOT_FOUND_ERROR(40400, "请求数据不存在"),
    FORBIDDEN_ERROR(40300, "禁止访问"),
    SYSTEM_ERROR(50000, "系统内部异常"),
    OPERATION_ERROR(50001, "操作失败");

    /**
     * 状态码
     */
    private final int code;

    /**
     * 信息
     */
    private final String message;

    // 这是构造函数
    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

}

