package com.pic.cloudpicturebackend.model.dto.spaceuser;

import lombok.Data;

import java.io.Serializable;

/**
 * 创建空间成员请求
 */
@Data
public class SpaceUserAddRequest implements Serializable {

    /**
     * 空间 id
     */
    private Long spaceId;

    /**
     * 用户 id
     */
    private Long userId;

    /**
     * 空间角色: viewer/editor/admin
     */
    private Integer spaceRole;

    private static final long serialVersionUID = 1L;
}
