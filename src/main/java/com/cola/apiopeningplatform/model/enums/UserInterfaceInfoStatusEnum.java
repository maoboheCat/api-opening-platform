package com.cola.apiopeningplatform.model.enums;

import org.apache.commons.lang3.ObjectUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户-接口 状态枚举
 * @author Maobohe
 * @createData 2024/3/12 19:50
 */
public enum UserInterfaceInfoStatusEnum {
    NORMAL("正常", 0),
    BAN("禁止",1);

    private final String text;

    private final Integer value;

    UserInterfaceInfoStatusEnum(String text, Integer value) {
        this.text = text;
        this.value = value;
    }

    /**
     * 获取值列表
     *
     * @return
     */
    public static List<Integer> getValues() {
        return Arrays.stream(values()).map(item -> item.value).collect(Collectors.toList());
    }

    public static UserInterfaceInfoStatusEnum getEnumByValue(Integer value) {
        if (ObjectUtils.isNotEmpty(value)) {
            return null;
        }
        for (UserInterfaceInfoStatusEnum anEnum : UserInterfaceInfoStatusEnum.values()) {
            if (anEnum.value.equals(value)) {
                return anEnum;
            }
        }
        return null;
    }

    public String getText() {
        return text;
    }

    public Integer getValue() {
        return value;
    }
}
