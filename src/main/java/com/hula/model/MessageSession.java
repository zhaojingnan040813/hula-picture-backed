package com.hula.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 私信会话表
 */
@TableName(value = "message_session")
@Data
public class MessageSession implements Serializable {
    
    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 会话唯一标识（两个用户ID拼接）
     */
    private String sessionKey;

    /**
     * 发起会话的用户ID
     */
    private Long fromUserId;

    /**
     * 接收会话的用户ID
     */
    private Long toUserId;

    /**
     * 最后一条消息ID
     */
    private Long lastMessageId;

    /**
     * 未读消息数
     */
    private Integer unreadCount;

    /**
     * 最后活跃时间
     */
    private Date lastActiveTime;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 是否删除 0-否 1-是
     */
    @TableLogic
    private Integer isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
} 