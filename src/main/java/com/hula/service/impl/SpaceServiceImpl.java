package com.hula.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hula.exception.BusinessException;
import com.hula.exception.ErrorCode;
import com.hula.exception.ThrowUtils;
import com.hula.mapper.SpaceMapper;
import com.hula.model.Space;
import com.hula.model.User;
import com.hula.model.dto.space.SpaceAddRequest;
import com.hula.model.dto.space.SpaceQueryRequest;
import com.hula.model.enums.SpaceLevelEnum;
import com.hula.model.vo.SpaceVO;
import com.hula.model.vo.UserVO;
import com.hula.service.SpaceService;
import com.hula.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Administrator
 * @description 针对表【space(空间)】的数据库操作Service实现
 * @createDate 2025-03-05 16:40:18
 */
@Service
public class SpaceServiceImpl extends ServiceImpl<SpaceMapper, Space>
        implements SpaceService {

    @Resource
    UserService userService;

    @Resource
    private TransactionTemplate transactionTemplate;//不同于声明式事务，这个是编程式事务

    @Override
    public QueryWrapper<Space> getQueryWrapper(SpaceQueryRequest spaceQueryRequest) {
        QueryWrapper<Space> queryWrapper = new QueryWrapper<>();
        if (spaceQueryRequest == null) {
            return queryWrapper;
        }
        // 从对象中取值
        Long id = spaceQueryRequest.getId();
        Long userId = spaceQueryRequest.getUserId();
        String spaceName = spaceQueryRequest.getSpaceName();
        Integer spaceLevel = spaceQueryRequest.getSpaceLevel();
        String sortField = spaceQueryRequest.getSortField();
        String sortOrder = spaceQueryRequest.getSortOrder();
        // 拼接查询条件
        queryWrapper.eq(ObjUtil.isNotEmpty(id), "id", id);
        queryWrapper.eq(ObjUtil.isNotEmpty(userId), "userId", userId);
        queryWrapper.like(StrUtil.isNotBlank(spaceName), "spaceName", spaceName);
        queryWrapper.eq(ObjUtil.isNotEmpty(spaceLevel), "spaceLevel", spaceLevel);
        // 排序
        queryWrapper.orderBy(StrUtil.isNotEmpty(sortField), sortOrder.equals("ascend"), sortField);
        return queryWrapper;
    }

    /**
     * 获取空间包装类（单条）
     *
     * @param space
     * @param request
     * @return
     */
    @Override
    public SpaceVO getSpaceVO(Space space, HttpServletRequest request) {
        // 对象转封装类
        SpaceVO spaceVO = SpaceVO.objToVo(space);
        // 关联查询用户信息
        Long userId = space.getUserId();
        if (userId != null && userId > 0) {
            User user = userService.getById(userId);
            UserVO userVO = userService.getUserVO(user);
            spaceVO.setUser(userVO);
        }
        return spaceVO;

    }

    @Override
    public Page<SpaceVO> getSpaceVOPage(Page<Space> spacePage, HttpServletRequest request) {
        List<Space> spaceList = spacePage.getRecords();
        Page<SpaceVO> spaceVOPage = new Page<>(spacePage.getCurrent(), spacePage.getSize(), spacePage.getTotal());
        if (CollUtil.isEmpty(spaceList)) {
            return spaceVOPage;
        }
        // 对象列表 => 封装对象列表
        List<SpaceVO> spaceVOList = spaceList.stream()
                .map(SpaceVO::objToVo)
                .collect(Collectors.toList());
        // 1. 关联查询用户信息
        // 1,2,3,4
        Set<Long> userIdSet = spaceList.stream().map(Space::getUserId).collect(Collectors.toSet());
        // 1 => user1, 2 => user2
        Map<Long, List<User>> userIdUserListMap = userService.listByIds(userIdSet).stream()
                .collect(Collectors.groupingBy(User::getId));
        // 2. 填充信息
        spaceVOList.forEach(spaceVO -> {
            Long userId = spaceVO.getUserId();
            User user = null;
            if (userIdUserListMap.containsKey(userId)) {
                user = userIdUserListMap.get(userId).get(0);
            }
            spaceVO.setUser(userService.getUserVO(user));
        });
        spaceVOPage.setRecords(spaceVOList);
        return spaceVOPage;
    }

    @Override
    public void validSpace(Space space, boolean add) {
        ThrowUtils.throwIf(space == null, ErrorCode.PARAMS_ERROR);
        //从对象中取值，取到的值最后都要校验一些是否为空
        String spaceName = space.getSpaceName();
        Integer spaceLevel = space.getSpaceLevel();
        //根据级别水平来获取到具体的枚举值
        SpaceLevelEnum spaceLevelEnum = SpaceLevelEnum.getEnumByValue(spaceLevel);//因为普通版专业版，他们所能具有的特性在数据库中是不存在的
//创建时候校验
        if (add) {
            if (StrUtil.isBlank(spaceName)) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "空间名称不能为空");
            }

//            if (StrUtil.isBlank(spaceName)){
//                校验字符串是否为空就要用是否为Null，而不是用String 工具的 isBlank
//            }

            if (spaceLevel == null) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "空间级别不能为空");

            }
        }
        // 不同情况下的进行校验
        // 修改数据时，空间名称进行校验
        if (StrUtil.isNotBlank(spaceName) && spaceName.length() > 30) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "空间名称过长");
        }
        // 修改数据时，空间级别进行校验
        if (spaceLevel != null && spaceLevelEnum == null) {//这里的枚举值虽然没有具体到枚举值
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "空间级别不存在");
        }


    }

    @Override
    public void fillSpaceBySpaceLevel(Space space) {
        SpaceLevelEnum spaceLevelEnum = SpaceLevelEnum.getEnumByValue(space.getSpaceLevel());
        if (spaceLevelEnum != null) {
            long maxSize = spaceLevelEnum.getMaxSize();
            if (space.getMaxSize() == null) {//如果前端没有传进来最大容量
                space.setMaxSize(maxSize);

            }
            long maxCount = spaceLevelEnum.getMaxCount();
            if (space.getMaxCount() == null) {
                space.setMaxCount(maxCount);
            }
        }

    }

    /**
     * 创建空间
     *
     * @param spaceAddRequest
     * @param loginUser
     * @return
     */
    @Override
    public long addSpace(SpaceAddRequest spaceAddRequest, User loginUser) {
        Space space = new Space();
        BeanUtil.copyProperties(spaceAddRequest, space);
        if (StrUtil.isBlank(space.getSpaceName())) {
            space.setSpaceName("默认空间");
        }
        if (space.getSpaceLevel() == null) {
            space.setSpaceLevel(SpaceLevelEnum.COMMON.getValue());//它的具体值有 0 1  2
        }

        this.fillSpaceBySpaceLevel(space);
        this.validSpace(space, true);
        Long userId = loginUser.getId();
        space.setUserId(userId);
        // 也就是普要创建高级的空间并且还不是管理员
        if (SpaceLevelEnum.COMMON.getValue() != space.getSpaceLevel() && !userService.isAdmin(loginUser)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "无权限创建指定级别的空间");
        }
        String lock = String.valueOf(userId).intern();//这个intern的作用是让相同的String指向同一个对象
//      每个用户一把锁而不是整个方法一把锁，这样可以提高性能
//        Long newSpanceId;
        synchronized (lock) {//一个用户在这个方法没有执行完之前只能创建一个空间
             Long newSpanceId = transactionTemplate.execute(status -> {
                boolean exists = this.lambdaQuery().eq(Space::getUserId, userId).exists();
                //如果已经有空间，就不再创建
                ThrowUtils.throwIf(exists, ErrorCode.OPERATION_ERROR, "每个用户只能有一个空间");
                //创建
                boolean result = this.save(space);
                ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR, "保存到数据库失败");
                return space.getId();
            });
//            return newSpanceId;
            return Optional.ofNullable(newSpanceId).orElse(-1L);

        }


    }


}




