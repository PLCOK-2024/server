package com.plcok.notification.entity;

import com.plcok.common.BaseEntity;
import com.plcok.notification.dto.request.NotificationTokenRequest;
import com.plcok.user.entity.User;
import com.plcok.notification.entity.enumerated.DeviceType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "devices")
@AttributeOverrides({
        @AttributeOverride(name = "createdAt", column = @Column(name = "created_at", nullable = false)),
        @AttributeOverride(name = "updatedAt", column = @Column(name = "updated_at"))
})
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Device extends BaseEntity {
    @NotNull
    @Column(name = "device_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private DeviceType deviceType;

    @Size(max = 200)
    @NotNull
    @Column(name = "device_token", nullable = false, length = 200)
    private String deviceToken;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "device")
    private Set<DeviceNotification> deviceNotifications = new LinkedHashSet<>();

    public static Device of(NotificationTokenRequest request, User user) {
        return builder()
                .deviceType(request.getDeviceType())
                .deviceToken(request.getDeviceToken())
                .user(user)
                .build();
    }
}