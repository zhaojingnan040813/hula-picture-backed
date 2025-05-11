package com.hula.model.dto.message;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 发送消息请求
 */
@Data
@ApiModel(value = "发送消息请求")
public class MessageSendRequest implements Serializable {
    
    /**
     * 接收者ID
     */
    @NotNull(message = "接收者ID不能为空")
    @ApiModelProperty(value = "接收者ID", required = true)
    private Long receiverId;
    
    /**
     * 消息内容
     */
    @NotBlank(message = "消息内容不能为空")
    @ApiModelProperty(value = "消息内容", required = true)
    private String content;
    
    /**
     * 消息类型：0-文本，1-图片，2-文件
     */
    @ApiModelProperty(value = "消息类型：0-文本，1-图片，2-文件", required = true)
    private Integer contentType = 0;
    
    /**
     * 媒体URL，如图片或文件链接
     */
    @ApiModelProperty(value = "媒体URL，如图片或文件链接")
    private String mediaUrl;
    
    private static final long serialVersionUID = 1L;
} 