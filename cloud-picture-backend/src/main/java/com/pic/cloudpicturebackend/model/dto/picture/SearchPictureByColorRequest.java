package com.pic.cloudpicturebackend.model.dto.picture;

import lombok.Data;

import java.io.Serializable;

/**
 * 按颜色搜索图片请求
 */
@Data
public class SearchPictureByColorRequest implements Serializable {

    /**
     * 空间id
     */
    private Long spaceId;

    /**
     * 图片主色调
     */
    private String picColor;

    private static final long serialVersionUID = 1L;
}
