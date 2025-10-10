package com.pic.cloudpicturebackend;

import com.pic.cloudpicturebackend.model.entity.User;
import com.pic.cloudpicturebackend.model.enums.UserRoleEnum;
import com.pic.cloudpicturebackend.service.UserService;
import com.pic.cloudpicturebackend.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class CloudPictureBackendApplicationTests {

    @Resource
    private UserService userService;

    @Test
    void contextLoads() {
        User user = new User();
        user.setUserAccount("root");
        String password = "12345678";
        String encryptPassword = userService.getEncryptPassword(password);
        user.setUserPassword(encryptPassword);
        user.setUserName("测试用户");
        user.setUserRole(UserRoleEnum.USER.getValue());
        userService.save(user);
    }

}
