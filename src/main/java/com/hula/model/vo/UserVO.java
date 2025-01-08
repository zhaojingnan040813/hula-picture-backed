package com.hula.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * VO 包通常用来存放脱敏后的数据
 */
@Data
public class UserVO implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * 账号
     */
    private String userAccount;

    /**
     * 用户昵称
     */
    private String userName;

    /**
     * 用户头像
     */
    private String userAvatar;

    /**
     * 用户简介
     */
    private String userProfile;

    /**
     * 用户角色：user/admin
     */
    private String userRole;

    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 一般实现了 Serializable 接口的都会有一个 serialVersionUID
     */
    private static final long serialVersionUID = 1L;
}
