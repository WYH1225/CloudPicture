package com.pic.cloudpicturebackend.model.enums;

import cn.hutool.core.util.ObjUtil;
import lombok.Getter;

/**
 * 空间类型枚举类
 */
@Getter
public enum SpaceTypeEnum {

    PRIVATE("私有空间", 0),
    TEAM("团队空间", 1);

    private final String text;

    private final int value;

    SpaceTypeEnum(String text, int value) {
        this.text = text;
        this.value = value;
    }

    /**
     * 根据value获取枚举
     *
     * @param value
     * @return
     */
    public static SpaceTypeEnum getEnumByValue(Integer value) {
        if (ObjUtil.isEmpty(value)) {
            return null;
        }
        for (SpaceTypeEnum spaceTypeEnum : values()) {
            if (spaceTypeEnum.value == value) {
                return spaceTypeEnum;
            }
        }
        return null;
    }
}
