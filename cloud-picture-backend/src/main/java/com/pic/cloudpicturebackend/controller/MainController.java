package com.pic.cloudpicturebackend.controller;

import com.pic.cloudpicturebackend.annotation.AuthCheck;
import com.pic.cloudpicturebackend.common.BaseResponse;
import com.pic.cloudpicturebackend.common.ResultUtils;
import com.pic.cloudpicturebackend.constant.UserConstant;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class MainController {

    /**
     * 健康检测
     *
     * @return
     */
    @GetMapping("/health")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<String> health() {
        return ResultUtils.success("ok");
    }
}
