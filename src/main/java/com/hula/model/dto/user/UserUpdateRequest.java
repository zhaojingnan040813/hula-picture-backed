package com.hula.model.dto.user;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author: 赵景南
 * @Date: 2025/1/8 17:50
 * @Version: v1.0.0
 * @Description: 略
 **/
@Data
public class UserUpdateRequest implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * 用户昵称
     */
    private String userName;

    /**
     * 用户头像
     */
    private String userAvatar;

    /**
     * 简介
     */
    private String userProfile;

    /**
     * 用户角色：user/admin
     */
    private String userRole;

    private static final long serialVersionUID = 1L;
}

