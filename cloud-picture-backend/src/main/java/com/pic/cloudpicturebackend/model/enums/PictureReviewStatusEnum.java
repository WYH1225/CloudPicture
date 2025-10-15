package com.pic.cloudpicturebackend.model.enums;

import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 图片审核状态枚举
 */
@Getter
public enum PictureReviewStatusEnum {

    REVIEWING("待审核", 0),
    PASS("通过", 1),
    REJECT("拒绝", 2);

    private final String text;

    private final int value;

    PictureReviewStatusEnum(String text, int value) {
        this.text = text;
        this.value = value;
    }

    /**
     * value 到枚举的映射 Map
     */
    private static final Map<Integer, PictureReviewStatusEnum> VALUE_MAP = Arrays.stream(PictureReviewStatusEnum.values())
            .collect(Collectors.toMap(PictureReviewStatusEnum::getValue, Function.identity()));

    /**
     * 根据 value 获取枚举
     * @param value 枚举值的 value
     * @return 枚举值
     */
    public static PictureReviewStatusEnum getEnumByValue(Integer value) {
        if (ObjectUtils.isEmpty(value)) {
            return null;
        }
        return VALUE_MAP.get(value);
    }
}
