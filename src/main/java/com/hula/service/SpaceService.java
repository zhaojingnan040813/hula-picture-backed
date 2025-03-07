package com.hula.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hula.model.Space;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hula.model.User;
import com.hula.model.dto.space.SpaceAddRequest;
import com.hula.model.dto.space.SpaceQueryRequest;
import com.hula.model.vo.SpaceVO;

import javax.servlet.http.HttpServletRequest;

/**
* @author Administrator
* @description 针对表【space(空间)】的数据库操作Service
* @createDate 2025-03-05 16:40:18
*/
public interface SpaceService extends IService<Space> {


    /**
     * 获取查询对象
     *
     * @param spaceQueryRequest
     * @return
     */
    QueryWrapper<Space> getQueryWrapper(SpaceQueryRequest spaceQueryRequest);
    /**
     * 获取空间包装类（单条）
     *
     * @param space
     * @param request
     * @return
     */
    SpaceVO getSpaceVO(Space space, HttpServletRequest request);
    /**
     * 获取空间包装类（分页）
     *
     * @param spacePage
     * @param request
     * @return
     */
    Page<SpaceVO> getSpaceVOPage(Page<Space> spacePage, HttpServletRequest request);

    /**
     *
     * @param space
     * @param add 是否为创建时校验
     */
    void validSpace(Space space,boolean add);

    /**
     * 根据空间级别填充空间对象
     * @param space
     */
    void  fillSpaceBySpaceLevel(Space space);

    /**
     * 创建空间
     * @param spaceAddRequest
     * @param loginUser
     * @return
     */
    long addSpace(SpaceAddRequest spaceAddRequest, User loginUser);





}
