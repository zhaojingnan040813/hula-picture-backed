package com.hula.model.dto.user;

/**
 * @Author: 赵景南
 * @Date: 2025/1/8 17:49
 * @Version: v1.0.0
 * @Description: 略
 **/
import lombok.Data;

import java.io.Serializable;

/**
 * 用户登录请求
 */
@Data
public class UserLoginRequest implements Serializable {

    private static final long serialVersionUID = 8735650154179439661L;

    /**
     * 账号
     */
    private String userAccount;

    /**
     * 密码
     */
    private String userPassword;
}
