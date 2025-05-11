-- 测试数据：插入测试用户（如果尚未添加）
-- 注意：如果已经有用户数据，可以跳过这部分
INSERT INTO `user` (id, userAccount, userPassword, userName, userAvatar, userProfile, userRole)
VALUES
(101, 'test_user1', '$2a$10$DP4lJWbZs0rJcFGbBlk8Yu2x1K9WnCZlwTFwrLBmQzx8VgCZCEE.W', '测试用户1', 'https://joeschmoe.io/api/v1/random', '这是测试用户1', 'user'),
(102, 'test_user2', '$2a$10$DP4lJWbZs0rJcFGbBlk8Yu2x1K9WnCZlwTFwrLBmQzx8VgCZCEE.W', '测试用户2', 'https://joeschmoe.io/api/v1/random', '这是测试用户2', 'user'),
(103, 'test_user3', '$2a$10$DP4lJWbZs0rJcFGbBlk8Yu2x1K9WnCZlwTFwrLBmQzx8VgCZCEE.W', '测试用户3', 'https://joeschmoe.io/api/v1/random', '这是测试用户3', 'user'),
(104, 'test_user4', '$2a$10$DP4lJWbZs0rJcFGbBlk8Yu2x1K9WnCZlwTFwrLBmQzx8VgCZCEE.W', '测试用户4', 'https://joeschmoe.io/api/v1/random', '这是测试用户4', 'user'),
(105, 'test_user5', '$2a$10$DP4lJWbZs0rJcFGbBlk8Yu2x1K9WnCZlwTFwrLBmQzx8VgCZCEE.W', '测试用户5', 'https://joeschmoe.io/api/v1/random', '这是测试用户5', 'user');

-- 注意：所有测试用户的密码都是 'password'

-- 创建会话数据
INSERT INTO `message_session` (id, sessionKey, fromUserId, toUserId, lastMessageId, unreadCount, lastActiveTime)
VALUES
(201, '101_102', 101, 102, 301, 2, DATE_SUB(NOW(), INTERVAL 1 HOUR)),
(202, '101_103', 101, 103, 304, 0, DATE_SUB(NOW(), INTERVAL 2 HOUR)),
(203, '102_104', 102, 104, 306, 1, DATE_SUB(NOW(), INTERVAL 3 HOUR)),
(204, '101_105', 105, 101, 309, 3, DATE_SUB(NOW(), INTERVAL 30 MINUTE));

-- 创建消息内容数据
INSERT INTO `message_content` (id, sessionId, senderId, receiverId, content, contentType, mediaUrl, isRead, readTime, createTime)
VALUES
-- 会话1：用户101和用户102
(301, 201, 101, 102, '你好，这是一条测试消息', 0, NULL, 1, DATE_SUB(NOW(), INTERVAL 1 HOUR), DATE_SUB(NOW(), INTERVAL 2 HOUR)),
(302, 201, 102, 101, '收到，这是回复消息', 0, NULL, 1, DATE_SUB(NOW(), INTERVAL 50 MINUTE), DATE_SUB(NOW(), INTERVAL 1 HOUR)),
(303, 201, 101, 102, '我们来测试一下私信功能', 0, NULL, 0, NULL, DATE_SUB(NOW(), INTERVAL 30 MINUTE)),

-- 会话2：用户101和用户103
(304, 202, 101, 103, '嗨，用户3，我是用户1', 0, NULL, 1, DATE_SUB(NOW(), INTERVAL 1 HOUR), DATE_SUB(NOW(), INTERVAL 2 HOUR)),
(305, 202, 103, 101, '你好用户1，很高兴认识你', 0, NULL, 1, DATE_SUB(NOW(), INTERVAL 55 MINUTE), DATE_SUB(NOW(), INTERVAL 1 HOUR)),

-- 会话3：用户102和用户104
(306, 203, 102, 104, '用户4，这是一条图片消息测试', 1, 'https://picsum.photos/200/300', 1, DATE_SUB(NOW(), INTERVAL 2 HOUR), DATE_SUB(NOW(), INTERVAL 3 HOUR)),
(307, 203, 104, 102, '收到图片了，很好看', 0, NULL, 1, DATE_SUB(NOW(), INTERVAL 1 HOUR), DATE_SUB(NOW(), INTERVAL 2 HOUR)),
(308, 203, 102, 104, '这是一条文件消息测试', 2, 'https://example.com/files/document.pdf', 0, NULL, DATE_SUB(NOW(), INTERVAL 30 MINUTE)),

-- 会话4：用户101和用户105
(309, 204, 105, 101, '你好，用户1', 0, NULL, 0, NULL, DATE_SUB(NOW(), INTERVAL 30 MINUTE)),
(310, 204, 105, 101, '我是新来的用户5', 0, NULL, 0, NULL, DATE_SUB(NOW(), INTERVAL 25 MINUTE)),
(311, 204, 105, 101, '希望能和你交流', 0, NULL, 0, NULL, DATE_SUB(NOW(), INTERVAL 20 MINUTE));

-- 创建一些用户关系数据（可选）
INSERT INTO `user_relation` (id, userId, relatedUserId, relationType)
VALUES
(401, 101, 102, 0), -- 用户1将用户2添加为好友
(402, 102, 101, 0), -- 用户2将用户1添加为好友
(403, 101, 103, 0), -- 用户1将用户3添加为好友
(404, 101, 104, 1); -- 用户1将用户4添加到黑名单 