package com.pic.cloudpicturebackend.manager.websocket.model;

import com.pic.cloudpicturebackend.model.vo.user.UserVO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 图片编辑响应消息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PictureEditResponseMessage {

    /**
     * 消息类型：例如 "ENTER_EDIT", "EXIT_EDIT", "EDIT_ACTION", "INFO", "ERROR"
     */
    private String type;

    /**
     * 消息内容
     */
    private String message;

    /**
     * 执行的编辑动作（放大、缩小、旋转）
     */
    private String editAction;

    /**
     * 用户信息
     */
    private UserVO user;
}
