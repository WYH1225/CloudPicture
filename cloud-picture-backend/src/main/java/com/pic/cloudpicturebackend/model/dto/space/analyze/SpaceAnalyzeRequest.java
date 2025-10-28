package com.pic.cloudpicturebackend.model.dto.space.analyze;

import lombok.Data;

import java.io.Serializable;

/**
 * 通用空间分析请求
 */
@Data
public class SpaceAnalyzeRequest implements Serializable {

    /**
     * 空间id
     */
    private Long spaceId;

    /**
     * 是否查询公共图库
     */
    private boolean queryPublic;

    /**
     * 是否分析全部空间
     */
    private boolean queryAll;

    private static final long serialVersionUID = 1L;
}
