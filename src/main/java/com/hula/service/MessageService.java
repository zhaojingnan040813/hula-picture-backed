package com.hula.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hula.model.dto.message.MessageContentDTO;
import com.hula.model.dto.message.MessageQueryRequest;
import com.hula.model.dto.message.MessageSendRequest;
import com.hula.model.dto.message.MessageSessionDTO;

import java.util.List;

/**
 * 消息服务接口
 */
public interface MessageService {

    /**
     * 发送消息
     *
     * @param request 发送消息请求
     * @param senderId 发送者ID
     * @return 消息ID
     */
    Long sendMessage(MessageSendRequest request, Long senderId);

    /**
     * 获取消息会话列表
     *
     * @param userId 用户ID
     * @return 会话列表
     */
    List<MessageSessionDTO> listSessions(Long userId);

    /**
     * 获取指定会话的消息列表
     *
     * @param request 消息查询请求
     * @param userId 当前用户ID
     * @return 消息分页列表
     */
    Page<MessageContentDTO> listMessages(MessageQueryRequest request, Long userId);

    /**
     * 标记消息为已读
     *
     * @param sessionId 会话ID
     * @param userId 用户ID
     * @return 是否成功
     */
    boolean markSessionAsRead(Long sessionId, Long userId);

    /**
     * 删除会话
     *
     * @param sessionId 会话ID
     * @param userId 用户ID
     * @return 是否成功
     */
    boolean deleteSession(Long sessionId, Long userId);

    /**
     * 获取未读消息总数
     *
     * @param userId 用户ID
     * @return 未读消息数
     */
    long countUnreadMessages(Long userId);
} 