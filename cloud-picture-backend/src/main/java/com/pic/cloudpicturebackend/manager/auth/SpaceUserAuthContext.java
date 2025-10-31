package com.pic.cloudpicturebackend.manager.auth;

import com.pic.cloudpicturebackend.model.entity.Picture;
import com.pic.cloudpicturebackend.model.entity.Space;
import com.pic.cloudpicturebackend.model.entity.SpaceUser;
import lombok.Data;

/**
 * 空间成员权限上下文：表示用户在特定空间内的授权上下文，包括关联的图片、空间和用户信息
 */
@Data
public class SpaceUserAuthContext {

    /**
     * 临时参数，不同请求对应的 id 可能不同
     */
    private Long id;

    /**
     * 图片 ID
     */
    private Long pictureId;

    /**
     * 空间 ID
     */
    private Long spaceId;

    /**
     * 空间成员 ID
     */
    private Long spaceUserId;

    /**
     * 图片信息
     */
    private Picture picture;

    /**
     * 空间信息
     */
    private Space space;

    /**
     * 空间成员信息
     */
    private SpaceUser spaceUser;
}
