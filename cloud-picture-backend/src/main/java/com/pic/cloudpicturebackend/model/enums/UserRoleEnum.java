package com.pic.cloudpicturebackend.model.enums;

import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 用户角色枚举
 */
@Getter
public enum UserRoleEnum {

    USER("用户", "user"),
    ADMIN("管理员", "admin");

    private final String text;

    private final String value;

    UserRoleEnum(String text, String value) {
        this.text = text;
        this.value = value;
    }

    /**
     * value 到枚举的映射 Map
     */
    private static final Map<String, UserRoleEnum> VALUE_MAP = Arrays.stream(UserRoleEnum.values())
            .collect(Collectors.toMap(UserRoleEnum::getValue, Function.identity()));

    /**
     * 根据 value 获取枚举
     * @param value 枚举值的 value
     * @return 枚举值
     */
    public static UserRoleEnum getEnumByValue(String value) {
        if (ObjectUtils.isEmpty(value)) {
            return null;
        }
        return VALUE_MAP.get(value);
    }
}
