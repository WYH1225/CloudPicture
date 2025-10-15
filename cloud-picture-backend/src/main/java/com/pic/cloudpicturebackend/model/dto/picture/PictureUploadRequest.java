package com.pic.cloudpicturebackend.model.dto.picture;

import lombok.Data;

import java.io.Serializable;

/**
 * 创建picture请求
 */
@Data
public class PictureUploadRequest implements Serializable {

    /**
     * 图片 id（用于修改）
     */
    private Long id;

    /**
     * 文件地址
     */
    private String fileUrl;

    private static final long serialVersionUID = 1L;
}
