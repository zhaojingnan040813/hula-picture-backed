package com.hula.model.dto.message;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 私信内容DTO
 */
@Data
public class MessageContentDTO implements Serializable {
    
    /**
     * 消息ID
     */
    private Long id;
    
    /**
     * 会话ID
     */
    private Long sessionId;
    
    /**
     * 发送者ID
     */
    private Long senderId;
    
    /**
     * 发送者名称
     */
    private String senderName;
    
    /**
     * 发送者头像
     */
    private String senderAvatar;
    
    /**
     * 接收者ID
     */
    private Long receiverId;
    
    /**
     * 消息内容
     */
    private String content;
    
    /**
     * 消息类型：0-文本，1-图片，2-文件
     */
    private Integer contentType;
    
    /**
     * 媒体URL，如图片或文件链接
     */
    private String mediaUrl;
    
    /**
     * 是否已读：0-未读，1-已读
     */
    private Integer isRead;
    
    /**
     * 发送时间
     */
    private Date createTime;
    
    /**
     * 是否自己发送的消息
     */
    private Boolean isSelf;
    
    private static final long serialVersionUID = 1L;
} 