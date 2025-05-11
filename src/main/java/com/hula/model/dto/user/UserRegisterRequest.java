package com.hula.model.dto.user;

import lombok.Data;

import java.io.Serializable;

/**
 * 为每一个接口定义一个专门的类来接收请求参数
 */
@Data
public class UserRegisterRequest implements Serializable {

    private static final long serialVersionUID = 3191241716373120793L;

    /**
     * 账号
     */
    private String userAccount;

    /**
     * 密码
     */
    private String userPassword;

    /**
     * 确认密码
     */
    private String checkPassword;
    
    /**
     * 用户角色：user/admin
     */
    private String userRole;
}
