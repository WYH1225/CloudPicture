package com.pic.cloudpicturebackend.manager.websocket.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 图片编辑请求消息
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PictureEditRequestMessage {

    /**
     * 消息类型：例如 "ENTER_EDIT", "EXIT_EDIT", "EDIT_ACTION"
     */
    private String type;

    /**
     * 执行的编辑动作（放大、缩小、旋转）
     */
    private String editAction;
}
