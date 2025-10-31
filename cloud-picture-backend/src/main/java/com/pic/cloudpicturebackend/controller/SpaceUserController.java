package com.pic.cloudpicturebackend.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjUtil;
import com.pic.cloudpicturebackend.common.BaseResponse;
import com.pic.cloudpicturebackend.common.DeleteRequest;
import com.pic.cloudpicturebackend.common.ResultUtils;
import com.pic.cloudpicturebackend.exception.BusinessException;
import com.pic.cloudpicturebackend.exception.ErrorCode;
import com.pic.cloudpicturebackend.exception.ThrowUtils;
import com.pic.cloudpicturebackend.manager.auth.annotation.SaSpaceCheckPermission;
import com.pic.cloudpicturebackend.manager.auth.model.SpaceUserPermissionConstant;
import com.pic.cloudpicturebackend.model.dto.spaceuser.SpaceUserAddRequest;
import com.pic.cloudpicturebackend.model.dto.spaceuser.SpaceUserEditRequest;
import com.pic.cloudpicturebackend.model.dto.spaceuser.SpaceUserQueryRequest;
import com.pic.cloudpicturebackend.model.entity.SpaceUser;
import com.pic.cloudpicturebackend.model.entity.User;
import com.pic.cloudpicturebackend.model.vo.spaceuser.SpaceUserVO;
import com.pic.cloudpicturebackend.service.SpaceUserService;
import com.pic.cloudpicturebackend.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/spaceUser")
@Slf4j
public class SpaceUserController {

    @Resource
    private SpaceUserService spaceUserService;

    @Resource
    private UserService userService;

    /**
     * 添加空间成员
     */
    @PostMapping("/add")
    @SaSpaceCheckPermission(value = SpaceUserPermissionConstant.SPACE_USER_MANAGE)
    public BaseResponse<Long> addSapceUser(@RequestBody SpaceUserAddRequest spaceUserAddRequest) {
        ThrowUtils.throwIf(spaceUserAddRequest == null, ErrorCode.PARAMS_ERROR);
        return ResultUtils.success(spaceUserService.addUserSpace(spaceUserAddRequest));
    }

    /**
     * 删除空间成员
     */
    @PostMapping("/delete")
    @SaSpaceCheckPermission(value = SpaceUserPermissionConstant.SPACE_USER_MANAGE)
    public BaseResponse<Boolean> deleteSpaceUser(@RequestBody DeleteRequest deleteRequest) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Long id = deleteRequest.getId();
        SpaceUser oldSpaceUser = spaceUserService.getById(id);
        ThrowUtils.throwIf(oldSpaceUser == null, ErrorCode.NOT_FOUND_ERROR);
        // 操作数据库
        boolean result = spaceUserService.removeById(id);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    /**
     * 查询某个成员在某个空间下的信息
     */
    @PostMapping("/get")
    @SaSpaceCheckPermission(value = SpaceUserPermissionConstant.SPACE_USER_MANAGE)
    public BaseResponse<SpaceUser> getSpaceUser(@RequestBody SpaceUserQueryRequest spaceUserQueryRequest) {
        ThrowUtils.throwIf(spaceUserQueryRequest == null, ErrorCode.PARAMS_ERROR);
        Long userId = spaceUserQueryRequest.getUserId();
        Long spaceId = spaceUserQueryRequest.getSpaceId();
        ThrowUtils.throwIf(ObjUtil.hasEmpty(userId, spaceId), ErrorCode.PARAMS_ERROR);
        // 查询数据库
        SpaceUser spaceUser = spaceUserService.getOne(spaceUserService.getQueryWrapper(spaceUserQueryRequest));
        ThrowUtils.throwIf(spaceUser == null, ErrorCode.NOT_FOUND_ERROR);
        return ResultUtils.success(spaceUser);
    }

    /**
     * 查询成员信息列表
     */
    @PostMapping("/list")
    @SaSpaceCheckPermission(value = SpaceUserPermissionConstant.SPACE_USER_MANAGE)
    public BaseResponse<List<SpaceUserVO>> listSpaceUser(@RequestBody SpaceUserQueryRequest spaceUserQueryRequest) {
        ThrowUtils.throwIf(spaceUserQueryRequest == null, ErrorCode.PARAMS_ERROR);
        List<SpaceUser> spaceUserList = spaceUserService.list(spaceUserService.getQueryWrapper(spaceUserQueryRequest));
        return ResultUtils.success(spaceUserService.getSpaceUserVOList(spaceUserList));
    }

    /**
     * 编辑成员信息（设置权限）
     */
    @PostMapping("/edit")
    @SaSpaceCheckPermission(value = SpaceUserPermissionConstant.SPACE_USER_MANAGE)
    public BaseResponse<Boolean> editSpaceUser(@RequestBody SpaceUserEditRequest spaceUserEditRequest) {
        if (spaceUserEditRequest == null || spaceUserEditRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 将实体类和 DTO 进行转换
        SpaceUser spaceUser = new SpaceUser();
        BeanUtil.copyProperties(spaceUserEditRequest, spaceUser);
        // 数据校验
        spaceUserService.validSpaceUser(spaceUser, false);
        // 判断是否存在
        long id = spaceUserEditRequest.getId();
        SpaceUser oldSpaceUser = spaceUserService.getById(id);
        ThrowUtils.throwIf(oldSpaceUser == null, ErrorCode.NOT_FOUND_ERROR);
        // 操作数据库
        boolean result = spaceUserService.updateById(spaceUser);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    /**
     * 查询我加入的团队空间列表
     */
    @PostMapping("/list/my")
    public BaseResponse<List<SpaceUserVO>> listMyTeamSpace(HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        SpaceUserQueryRequest spaceUserQueryRequest = new SpaceUserQueryRequest();
        spaceUserQueryRequest.setUserId(loginUser.getId());
        List<SpaceUser> spaceUserList = spaceUserService.list(spaceUserService.getQueryWrapper(spaceUserQueryRequest));
        return ResultUtils.success(spaceUserService.getSpaceUserVOList(spaceUserList));
    }
}
