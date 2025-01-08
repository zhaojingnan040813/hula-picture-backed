package com.hula.model.dto.user;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author: 赵景南
 * @Date: 2025/1/8 17:48
 * @Version: v1.0.0
 * @Description: 略
 **/
@Data
public class UserAddRequest implements Serializable {

    /**
     * 用户昵称
     */
    private String userName;

    /**
     * 账号
     */
    private String userAccount;

    /**
     * 用户头像
     */
    private String userAvatar;

    /**
     * 用户简介
     */
    private String userProfile;

    /**
     * 用户角色: user, admin
     */
    private String userRole;

    private static final long serialVersionUID = 1L;
}
