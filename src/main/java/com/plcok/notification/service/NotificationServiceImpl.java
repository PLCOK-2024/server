package com.plcok.notification.service;

import com.plcok.notification.dto.request.NotificationTokenRequest;
import com.plcok.notification.entity.Device;
import com.plcok.notification.repository.DeviceRepository;
import com.plcok.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final DeviceRepository deviceRepository;

    @Override
    @Transactional
    public void saveDeviceToken(User user, NotificationTokenRequest request) {
        deviceRepository.save(Device.of(request, user));
    }
}
