package com.pic.cloudpicturebackend.manager.auth;

import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.pic.cloudpicturebackend.manager.auth.model.SpaceUserAuthConfig;
import com.pic.cloudpicturebackend.manager.auth.model.SpaceUserPermissionConstant;
import com.pic.cloudpicturebackend.manager.auth.model.SpaceUserRole;
import com.pic.cloudpicturebackend.model.entity.Space;
import com.pic.cloudpicturebackend.model.entity.SpaceUser;
import com.pic.cloudpicturebackend.model.entity.User;
import com.pic.cloudpicturebackend.model.enums.SpaceRoleEnum;
import com.pic.cloudpicturebackend.model.enums.SpaceTypeEnum;
import com.pic.cloudpicturebackend.service.SpaceUserService;
import com.pic.cloudpicturebackend.service.UserService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

/**
 * 空间成员权限管理
 */
@Component
public class SpaceUserAuthManager {

    @Resource
    private UserService userService;

    @Resource
    private SpaceUserService spaceUserService;

    public static final SpaceUserAuthConfig SPACE_USER_AUTH_CONFIG;

    static {
        String json = ResourceUtil.readUtf8Str("biz/spaceUserAuthConfig.json");
        SPACE_USER_AUTH_CONFIG = JSONUtil.toBean(json, SpaceUserAuthConfig.class);
    }

    /**
     * 根据角色获取权限列表
     *
     * @param spaceUserRole 空间角色
     * @return 权限列表
     */
    public List<String> getPermissionsByRole(String spaceUserRole) {
        if (StrUtil.isBlank(spaceUserRole)) {
            return Collections.emptyList();
        }
        SpaceUserRole role = SPACE_USER_AUTH_CONFIG.getRoles().stream()
                .filter(r -> r.getKey().equals(spaceUserRole))
                .findFirst()
                .orElse(null);
        if (role == null) {
            return Collections.emptyList();
        }
        return role.getPermissions();
    }

    /**
     * 获取权限列表
     *
     * @param space     空间
     * @param loginUser 登录用户
     * @return 权限列表
     */
    public List<String> getPermissionList(Space space, User loginUser) {
        if (loginUser == null) {
            return Collections.emptyList();
        }
        // 管理员权限
        List<String> ADMIN_PERMISSIONS = getPermissionsByRole(SpaceRoleEnum.ADMIN.getValue());
        // 公共图库
        if (space == null) {
            if (userService.isAdmin(loginUser)) {
                return ADMIN_PERMISSIONS;
            }
            return Collections.singletonList(SpaceUserPermissionConstant.PICTURE_VIEW);
        }
        SpaceTypeEnum spaceTypeEnum = SpaceTypeEnum.getEnumByValue(space.getSpaceType());
        if (spaceTypeEnum == null) {
            return Collections.emptyList();
        }
        // 根据空间类型获取相应的权限
        switch (spaceTypeEnum) {
            // 私有空间，仅本人和管理员有权限
            case PRIVATE:
                if (space.getUserId().equals(loginUser.getId()) || userService.isAdmin(loginUser)) {
                    return ADMIN_PERMISSIONS;
                } else {
                    return Collections.emptyList();
                }
            case TEAM:
                // 团队空间，查询 SpaceUser 并获取角色和权限
                SpaceUser spaceUser = spaceUserService.lambdaQuery()
                        .eq(SpaceUser::getUserId, loginUser.getId())
                        .eq(SpaceUser::getSpaceId, space.getId())
                        .one();
                if (spaceUser == null) {
                    return Collections.emptyList();
                } else {
                    return getPermissionsByRole(spaceUser.getSpaceRole());
                }
        }
        return Collections.emptyList();
    }
}
