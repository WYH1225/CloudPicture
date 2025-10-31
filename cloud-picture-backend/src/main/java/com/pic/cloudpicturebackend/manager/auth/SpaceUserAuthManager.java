package com.pic.cloudpicturebackend.manager.auth;

import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.pic.cloudpicturebackend.manager.auth.model.SpaceUserAuthConfig;
import com.pic.cloudpicturebackend.manager.auth.model.SpaceUserRole;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

/**
 * 空间成员权限管理
 */
@Component
public class SpaceUserAuthManager {

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
}
