package com.pic.cloudpicturebackend.service;

import com.pic.cloudpicturebackend.model.dto.UserLoginRequest;
import com.pic.cloudpicturebackend.model.dto.UserRegisterRequest;
import com.pic.cloudpicturebackend.model.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pic.cloudpicturebackend.model.vo.UserLoginVO;

import javax.servlet.http.HttpServletRequest;

/**
* @author wyh
* @description 针对表【user(用户)】的数据库操作Service
* @createDate 2025-10-09 19:31:44
*/
public interface UserService extends IService<User> {

    /**
     * 用户注册
     * @param userRegisterRequest 用户注册请求
     * @return 新用户 id
     */
    long userRegister(UserRegisterRequest userRegisterRequest);

    /**
     * 用户登录
     * @return 当前登录用户
     */
    UserLoginVO userLogin(UserLoginRequest userLoginRequest, HttpServletRequest request);

    /**
     * 获取加密后的密码
     * @param userPassword 用户密码
     * @return 加密后的密码
     */
    String getEncryptPassword(String userPassword);

    /**
     * 获取脱敏后的登录用户信息
     * @param user
     * @return
     */
    UserLoginVO getLoginUserVO(User user);
}
