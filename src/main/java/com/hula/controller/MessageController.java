package com.hula.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hula.annotation.AuthCheck;
import com.hula.common.BaseResponse;
import com.hula.common.ResultUtils;
import com.hula.exception.BusinessException;
import com.hula.exception.ErrorCode;
import com.hula.model.User;
import com.hula.model.dto.message.MessageContentDTO;
import com.hula.model.dto.message.MessageQueryRequest;
import com.hula.model.dto.message.MessageSendRequest;
import com.hula.model.dto.message.MessageSessionDTO;
import com.hula.service.MessageService;
import com.hula.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 私信接口
 */
@RestController
@RequestMapping("/message")
@Slf4j
@Api(tags = "私信接口")
public class MessageController {

    @Resource
    private MessageService messageService;

    @Resource
    private UserService userService;

    /**
     * 发送消息
     *
     * @param messageSendRequest 发送消息请求
     * @param request HTTP请求
     * @return 消息ID
     */
    @PostMapping("/send")
    @ApiOperation(value = "发送消息")
    public BaseResponse<Long> sendMessage(@RequestBody MessageSendRequest messageSendRequest,
                                         HttpServletRequest request) {
        if (messageSendRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        Long messageId = messageService.sendMessage(messageSendRequest, loginUser.getId());
        return ResultUtils.success(messageId);
    }

    /**
     * 获取会话列表
     *
     * @param request HTTP请求
     * @return 会话列表
     */
    @GetMapping("/session/list")
    @ApiOperation(value = "获取会话列表")
    public BaseResponse<List<MessageSessionDTO>> listSessions(HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        List<MessageSessionDTO> sessionList = messageService.listSessions(loginUser.getId());
        return ResultUtils.success(sessionList);
    }

    /**
     * 获取会话消息列表
     *
     * @param messageQueryRequest 消息查询请求
     * @param request HTTP请求
     * @return 消息列表
     */
    @GetMapping("/list")
    @ApiOperation(value = "获取会话消息列表")
    public BaseResponse<Page<MessageContentDTO>> listMessages(MessageQueryRequest messageQueryRequest,
                                                            HttpServletRequest request) {
        if (messageQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        Page<MessageContentDTO> messagePage = messageService.listMessages(messageQueryRequest, loginUser.getId());
        return ResultUtils.success(messagePage);
    }

    /**
     * 标记会话为已读
     *
     * @param sessionId 会话ID
     * @param request HTTP请求
     * @return 是否成功
     */
    @PostMapping("/session/read")
    @ApiOperation(value = "标记会话为已读")
    public BaseResponse<Boolean> markSessionAsRead(@RequestParam Long sessionId,
                                                  HttpServletRequest request) {
        if (sessionId == null || sessionId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        boolean result = messageService.markSessionAsRead(sessionId, loginUser.getId());
        return ResultUtils.success(result);
    }

    /**
     * 删除会话
     *
     * @param sessionId 会话ID
     * @param request HTTP请求
     * @return 是否成功
     */
    @PostMapping("/session/delete")
    @ApiOperation(value = "删除会话")
    public BaseResponse<Boolean> deleteSession(@RequestParam Long sessionId,
                                             HttpServletRequest request) {
        if (sessionId == null || sessionId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        boolean result = messageService.deleteSession(sessionId, loginUser.getId());
        return ResultUtils.success(result);
    }

    /**
     * 获取未读消息数
     *
     * @param request HTTP请求
     * @return 未读消息数
     */
    @GetMapping("/unread/count")
    @ApiOperation(value = "获取未读消息数")
    public BaseResponse<Long> countUnreadMessages(HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        long count = messageService.countUnreadMessages(loginUser.getId());
        return ResultUtils.success(count);
    }
} 