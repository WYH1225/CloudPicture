package com.pic.cloudpicturebackend.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pic.cloudpicturebackend.model.dto.spaceuser.SpaceUserAddRequest;
import com.pic.cloudpicturebackend.model.dto.spaceuser.SpaceUserQueryRequest;
import com.pic.cloudpicturebackend.model.entity.SpaceUser;
import com.pic.cloudpicturebackend.model.entity.User;
import com.pic.cloudpicturebackend.model.vo.spaceuser.SpaceUserVO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
* @description 针对表【space_user(空间用户关联)】的数据库操作Service
*/
public interface SpaceUserService extends IService<SpaceUser> {

    /**
     * 创建空间成员
     *
     * @param spaceUserAddRequest
     * @return
     */
    long addUserSpace(SpaceUserAddRequest spaceUserAddRequest);

    /**
     * 校验空间成员
     *
     * @param spaceUser
     * @param add
     */
    void validSpaceUser(SpaceUser spaceUser, boolean add);

    /**
     * 获取空间成员封装类（单条）
     *
     * @param spaceUser
     * @param request
     * @return
     */
    SpaceUserVO getSpaceUserVO(SpaceUser spaceUser, HttpServletRequest request);

    /**
     * 获取空间成员封装类（列表）
     *
     * @param spaceUserList
     * @return
     */
    List<SpaceUserVO> getSpaceUserVOList(List<SpaceUser> spaceUserList);

    /**
     * 获取查询对象
     *
     * @param spaceUserQueryRequest
     * @return
     */
    QueryWrapper<SpaceUser> getQueryWrapper(SpaceUserQueryRequest spaceUserQueryRequest);
}
