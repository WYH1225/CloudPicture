package com.pic.cloudpicturebackend.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pic.cloudpicturebackend.common.DeleteRequest;
import com.pic.cloudpicturebackend.model.dto.picture.PictureQueryRequest;
import com.pic.cloudpicturebackend.model.dto.space.SpaceAddRequest;
import com.pic.cloudpicturebackend.model.dto.space.SpaceQueryRequest;
import com.pic.cloudpicturebackend.model.entity.Picture;
import com.pic.cloudpicturebackend.model.entity.Space;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pic.cloudpicturebackend.model.entity.User;
import com.pic.cloudpicturebackend.model.vo.PictureVO;
import com.pic.cloudpicturebackend.model.vo.SpaceVO;

import javax.servlet.http.HttpServletRequest;

/**
* @description 针对表【space(空间)】的数据库操作Service
*/
public interface SpaceService extends IService<Space> {

    /**
     * 创建空间
     *
     * @param spaceAddRequest
     * @param loginUser
     * @return
     */
    long addSpace(SpaceAddRequest spaceAddRequest, User loginUser);

    /**
     * 校验空间
     *
     * @param space
     * @param add
     */
    void validSpace(Space space, boolean add);

    /**
     * 根据空间级别填充空间信息
     *
     * @param space
     */
    void fillSpaceBySpaceLevel(Space space);

    /**
     * 获取空间封装类（单条）
     *
     * @param space
     * @param request
     * @return
     */
    SpaceVO getSpaceVO(Space space, HttpServletRequest request);

    /**
     * 获取空间封装类（分页）
     *
     * @param spacePage
     * @return
     */
    Page<SpaceVO> getSpaceVOPage(Page<Space> spacePage);

    /**
     * 获取查询对象
     *
     * @param spaceQueryRequest
     * @return
     */
    QueryWrapper<Space> getQueryWrapper(SpaceQueryRequest spaceQueryRequest);

    boolean deleteSpace(DeleteRequest deleteRequest, HttpServletRequest request);
}
