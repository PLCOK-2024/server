package com.plcok.notification.service;

import com.plcok.notification.dto.request.NotificationTokenRequest;
import com.plcok.user.entity.User;

public interface NotificationService {
    void saveDeviceToken(User user, NotificationTokenRequest request);
}
