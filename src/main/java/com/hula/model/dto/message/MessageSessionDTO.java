package com.hula.model.dto.message;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 私信会话DTO
 */
@Data
public class MessageSessionDTO implements Serializable {
    
    /**
     * 会话ID
     */
    private Long sessionId;
    
    /**
     * 对方用户ID
     */
    private Long targetUserId;
    
    /**
     * 对方用户名称
     */
    private String targetUserName;
    
    /**
     * 对方用户头像
     */
    private String targetUserAvatar;
    
    /**
     * 最后一条消息内容
     */
    private String lastMessage;
    
    /**
     * 最后一条消息类型：0-文本，1-图片，2-文件
     */
    private Integer lastMessageType;
    
    /**
     * 未读消息数
     */
    private Integer unreadCount;
    
    /**
     * 最后活跃时间
     */
    private Date lastActiveTime;
    
    /**
     * 是否在线
     */
    private Boolean isOnline;
    
    private static final long serialVersionUID = 1L;
} 