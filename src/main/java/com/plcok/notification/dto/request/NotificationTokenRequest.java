package com.plcok.notification.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class NotificationTokenRequest {
    private String deviceToken;
    private String deviceType;
}
