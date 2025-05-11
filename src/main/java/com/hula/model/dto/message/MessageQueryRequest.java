package com.hula.model.dto.message;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 消息查询请求
 */
@Data
@ApiModel(value = "消息查询请求")
public class MessageQueryRequest implements Serializable {
    
    /**
     * 会话ID，如果为空则查询与目标用户的会话
     */
    @ApiModelProperty(value = "会话ID")
    private Long sessionId;
    
    /**
     * 目标用户ID，当sessionId为空时必填
     */
    @ApiModelProperty(value = "目标用户ID")
    private Long targetUserId;
    
    /**
     * 页码
     */
    @ApiModelProperty(value = "页码", required = true)
    @NotNull(message = "页码不能为空")
    private Integer pageNum = 1;
    
    /**
     * 每页条数
     */
    @ApiModelProperty(value = "每页条数", required = true)
    @NotNull(message = "每页条数不能为空")
    private Integer pageSize = 20;
    
    private static final long serialVersionUID = 1L;
} 