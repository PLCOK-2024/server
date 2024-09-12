package com.plcok.notification.entity.enumerated;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.plcok.common.error.BusinessException;
import com.plcok.common.error.ErrorCode;

public enum DeviceType {
    IOS,
    ANDROID;

    @JsonCreator
    public static DeviceType from(String type) {
        for (DeviceType deviceType : DeviceType.values()) {
            if (type.equals(deviceType.name())) {
                return deviceType;
            }
        }
        throw new BusinessException(ErrorCode.DEVICETYPE_NOT_VALID);
    }
}
