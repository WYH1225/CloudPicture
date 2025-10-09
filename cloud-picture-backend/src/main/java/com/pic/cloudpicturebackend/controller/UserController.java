package com.pic.cloudpicturebackend.controller;

import com.pic.cloudpicturebackend.common.BaseResponse;
import com.pic.cloudpicturebackend.common.ResultUtils;
import com.pic.cloudpicturebackend.exception.ErrorCode;
import com.pic.cloudpicturebackend.exception.ThrowUtils;
import com.pic.cloudpicturebackend.model.dto.UserLoginRequest;
import com.pic.cloudpicturebackend.model.dto.UserRegisterRequest;
import com.pic.cloudpicturebackend.model.vo.UserLoginVO;
import com.pic.cloudpicturebackend.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    /**
     * 用户注册
     */
    @PostMapping("/register")
    public BaseResponse<Long> userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
        ThrowUtils.throwIf(userRegisterRequest == null, ErrorCode.PARAMS_ERROR);
        return ResultUtils.success(userService.userRegister(userRegisterRequest));
    }

    @PostMapping("/login")
    public BaseResponse<UserLoginVO> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        ThrowUtils.throwIf(userLoginRequest == null, ErrorCode.PARAMS_ERROR);
        return ResultUtils.success(userService.userLogin(userLoginRequest, request));
    }
}
