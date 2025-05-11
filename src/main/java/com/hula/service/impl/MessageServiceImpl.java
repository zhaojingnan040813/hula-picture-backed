package com.hula.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hula.exception.BusinessException;
import com.hula.exception.ErrorCode;
import com.hula.mapper.MessageContentMapper;
import com.hula.mapper.MessageSessionMapper;
import com.hula.mapper.UserMapper;
import com.hula.model.MessageContent;
import com.hula.model.MessageSession;
import com.hula.model.User;
import com.hula.model.dto.message.MessageContentDTO;
import com.hula.model.dto.message.MessageQueryRequest;
import com.hula.model.dto.message.MessageSendRequest;
import com.hula.model.dto.message.MessageSessionDTO;
import com.hula.service.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 消息服务实现
 */
@Service
@Slf4j
public class MessageServiceImpl implements MessageService {

    @Resource
    private MessageSessionMapper messageSessionMapper;

    @Resource
    private MessageContentMapper messageContentMapper;

    @Resource
    private UserMapper userMapper;

    /**
     * 生成会话Key，格式为：较小用户ID_较大用户ID
     */
    private String generateSessionKey(Long userId1, Long userId2) {
        return userId1 < userId2 ? userId1 + "_" + userId2 : userId2 + "_" + userId1;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long sendMessage(MessageSendRequest request, Long senderId) {
        Long receiverId = request.getReceiverId();
        // 校验接收者是否存在
        User receiver = userMapper.selectById(receiverId);
        if (receiver == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "接收者不存在");
        }

        // 不能给自己发消息
        if (Objects.equals(senderId, receiverId)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "不能给自己发消息");
        }

        // 生成或获取会话
        String sessionKey = generateSessionKey(senderId, receiverId);
        MessageSession messageSession = getOrCreateSession(sessionKey, senderId, receiverId);

        // 创建消息
        MessageContent messageContent = new MessageContent();
        messageContent.setSessionId(messageSession.getId());
        messageContent.setSenderId(senderId);
        messageContent.setReceiverId(receiverId);
        messageContent.setContent(request.getContent());
        messageContent.setContentType(request.getContentType());
        messageContent.setMediaUrl(request.getMediaUrl());
        messageContent.setIsRead(0);
        messageContentMapper.insert(messageContent);

        // 更新会话信息
        messageSession.setLastMessageId(messageContent.getId());
        messageSession.setLastActiveTime(new Date());
        // 增加未读消息数
        messageSession.setUnreadCount(messageSession.getUnreadCount() + 1);
        messageSessionMapper.updateById(messageSession);

        return messageContent.getId();
    }

    /**
     * 获取或创建会话
     */
    private MessageSession getOrCreateSession(String sessionKey, Long fromUserId, Long toUserId) {
        // 查询会话是否存在
        LambdaQueryWrapper<MessageSession> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MessageSession::getSessionKey, sessionKey);
        MessageSession messageSession = messageSessionMapper.selectOne(queryWrapper);

        // 不存在则创建
        if (messageSession == null) {
            messageSession = new MessageSession();
            messageSession.setSessionKey(sessionKey);
            messageSession.setFromUserId(fromUserId);
            messageSession.setToUserId(toUserId);
            messageSession.setUnreadCount(0);
            messageSession.setLastActiveTime(new Date());
            messageSessionMapper.insert(messageSession);
        }

        return messageSession;
    }

    @Override
    public List<MessageSessionDTO> listSessions(Long userId) {
        // 查询用户相关的所有会话
        LambdaQueryWrapper<MessageSession> queryWrapper = new LambdaQueryWrapper<>();
        // 修复OR条件，使用括号正确分组
        queryWrapper.and(wrapper -> wrapper
                .eq(MessageSession::getFromUserId, userId)
                .or()
                .eq(MessageSession::getToUserId, userId));
        queryWrapper.orderByDesc(MessageSession::getLastActiveTime);
        // 添加条件：只查询未删除的会话
        queryWrapper.eq(MessageSession::getIsDelete, 0);
        List<MessageSession> sessions = messageSessionMapper.selectList(queryWrapper);

        if (sessions.isEmpty()) {
            return new ArrayList<>();
        }

        // 获取所有相关用户ID
        Set<Long> userIds = new HashSet<>();
        for (MessageSession session : sessions) {
            // 对方用户ID
            Long targetUserId = Objects.equals(session.getFromUserId(), userId) 
                    ? session.getToUserId() : session.getFromUserId();
            userIds.add(targetUserId);
        }

        // 批量获取用户信息
        List<User> users = userMapper.selectBatchIds(userIds);
        Map<Long, User> userMap = users.stream().collect(Collectors.toMap(User::getId, user -> user));

        // 获取最后一条消息
        Set<Long> lastMessageIds = sessions.stream()
                .map(MessageSession::getLastMessageId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        
        Map<Long, MessageContent> messageMap = new HashMap<>();
        if (!lastMessageIds.isEmpty()) {
            List<MessageContent> lastMessages = messageContentMapper.selectBatchIds(lastMessageIds);
            messageMap = lastMessages.stream().collect(Collectors.toMap(MessageContent::getId, message -> message, (v1, v2) -> v1));
        }

        // 组装DTO
        List<MessageSessionDTO> result = new ArrayList<>();
        for (MessageSession session : sessions) {
            MessageSessionDTO dto = new MessageSessionDTO();
            dto.setSessionId(session.getId());
            
            // 对方用户ID
            Long targetUserId = Objects.equals(session.getFromUserId(), userId) 
                    ? session.getToUserId() : session.getFromUserId();
            dto.setTargetUserId(targetUserId);
            
            // 设置对方用户信息
            User targetUser = userMap.get(targetUserId);
            if (targetUser != null) {
                dto.setTargetUserName(targetUser.getUserName());
                dto.setTargetUserAvatar(targetUser.getUserAvatar());
                // 简化处理，实际项目中可能需要通过Redis或其他机制判断在线状态
                dto.setIsOnline(false);
            }
            
            // 设置最后一条消息
            MessageContent lastMessage = messageMap.get(session.getLastMessageId());
            if (lastMessage != null) {
                dto.setLastMessage(lastMessage.getContent());
                dto.setLastMessageType(lastMessage.getContentType());
            }
            
            dto.setUnreadCount(session.getUnreadCount());
            dto.setLastActiveTime(session.getLastActiveTime());
            
            result.add(dto);
        }

        return result;
    }

    @Override
    public Page<MessageContentDTO> listMessages(MessageQueryRequest request, Long userId) {
        Long sessionId = request.getSessionId();
        Long targetUserId = request.getTargetUserId();
        
        // 根据目标用户ID查找会话
        if (sessionId == null && targetUserId != null) {
            String sessionKey = generateSessionKey(userId, targetUserId);
            LambdaQueryWrapper<MessageSession> sessionQuery = new LambdaQueryWrapper<>();
            sessionQuery.eq(MessageSession::getSessionKey, sessionKey);
            // 添加未删除条件
            sessionQuery.eq(MessageSession::getIsDelete, 0);
            MessageSession session = messageSessionMapper.selectOne(sessionQuery);
            
            if (session != null) {
                sessionId = session.getId();
            } else {
                // 如果会话不存在，返回空结果
                return new Page<>(request.getPageNum(), request.getPageSize());
            }
        }
        
        if (sessionId == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "会话ID和目标用户ID不能同时为空");
        }
        
        // 验证用户是否有权限访问该会话
        MessageSession session = messageSessionMapper.selectById(sessionId);
        if (session == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "会话不存在");
        }
        
        if (!Objects.equals(session.getFromUserId(), userId) && !Objects.equals(session.getToUserId(), userId)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR, "无权访问该会话");
        }
        
        // 查询消息
        LambdaQueryWrapper<MessageContent> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MessageContent::getSessionId, sessionId);
        // 添加未删除条件
        queryWrapper.eq(MessageContent::getIsDelete, 0);
        queryWrapper.orderByDesc(MessageContent::getCreateTime);
        
        Page<MessageContent> page = new Page<>(request.getPageNum(), request.getPageSize());
        Page<MessageContent> messagePage = messageContentMapper.selectPage(page, queryWrapper);
        
        // 查询用户信息
        Set<Long> userIds = messagePage.getRecords().stream()
                .map(MessageContent::getSenderId)
                .collect(Collectors.toSet());
        
        final Map<Long, User> userMap = new HashMap<>();
        if (!userIds.isEmpty()) {
            List<User> users = userMapper.selectBatchIds(userIds);
            userMap.putAll(users.stream().collect(Collectors.toMap(User::getId, user -> user)));
        }
        
        // 转换为DTO
        List<MessageContentDTO> records = messagePage.getRecords().stream().map(message -> {
            MessageContentDTO dto = new MessageContentDTO();
            BeanUtils.copyProperties(message, dto);
            
            // 设置发送者信息
            User sender = userMap.get(message.getSenderId());
            if (sender != null) {
                dto.setSenderName(sender.getUserName());
                dto.setSenderAvatar(sender.getUserAvatar());
            }
            
            // 判断是否是自己发送的消息
            dto.setIsSelf(Objects.equals(message.getSenderId(), userId));
            
            return dto;
        }).collect(Collectors.toList());
        
        // 创建新的页面对象
        Page<MessageContentDTO> result = new Page<>(messagePage.getCurrent(), messagePage.getSize(), messagePage.getTotal());
        result.setRecords(records);
        
        // 如果是查看消息，标记为已读
        markSessionAsRead(sessionId, userId);
        
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean markSessionAsRead(Long sessionId, Long userId) {
        // 验证会话是否存在
        LambdaQueryWrapper<MessageSession> sessionQuery = new LambdaQueryWrapper<>();
        sessionQuery.eq(MessageSession::getId, sessionId)
                .eq(MessageSession::getIsDelete, 0);
        MessageSession session = messageSessionMapper.selectOne(sessionQuery);
        
        if (session == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "会话不存在");
        }
        
        // 验证用户权限
        if (!Objects.equals(session.getFromUserId(), userId) && !Objects.equals(session.getToUserId(), userId)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR, "无权操作该会话");
        }
        
        // 标记消息为已读
        LambdaUpdateWrapper<MessageContent> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(MessageContent::getSessionId, sessionId)
                .eq(MessageContent::getReceiverId, userId)
                .eq(MessageContent::getIsRead, 0)
                .eq(MessageContent::getIsDelete, 0)  // 只更新未删除的消息
                .set(MessageContent::getIsRead, 1)
                .set(MessageContent::getReadTime, new Date());
        messageContentMapper.update(null, updateWrapper);
        
        // 更新会话未读数
        session.setUnreadCount(0);
        messageSessionMapper.updateById(session);
        
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteSession(Long sessionId, Long userId) {
        // 验证会话是否存在
        LambdaQueryWrapper<MessageSession> sessionQuery = new LambdaQueryWrapper<>();
        sessionQuery.eq(MessageSession::getId, sessionId)
                .eq(MessageSession::getIsDelete, 0);
        MessageSession session = messageSessionMapper.selectOne(sessionQuery);
        
        if (session == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "会话不存在");
        }
        
        // 验证用户权限
        if (!Objects.equals(session.getFromUserId(), userId) && !Objects.equals(session.getToUserId(), userId)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR, "无权操作该会话");
        }
        
        // 逻辑删除会话
        messageSessionMapper.deleteById(sessionId);
        
        // 逻辑删除相关消息
        LambdaUpdateWrapper<MessageContent> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(MessageContent::getSessionId, sessionId)
                .eq(MessageContent::getIsDelete, 0);  // 只删除未删除的消息
        messageContentMapper.delete(updateWrapper);
        
        return true;
    }

    @Override
    public long countUnreadMessages(Long userId) {
        // 查询所有未读会话数量
        LambdaQueryWrapper<MessageSession> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MessageSession::getToUserId, userId)
                .gt(MessageSession::getUnreadCount, 0)
                .eq(MessageSession::getIsDelete, 0);  // 只统计未删除的会话
        
        List<MessageSession> sessions = messageSessionMapper.selectList(queryWrapper);
        
        return sessions.stream()
                .mapToLong(MessageSession::getUnreadCount)
                .sum();
    }
} 