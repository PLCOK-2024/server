package com.example.demo.common.entity;

import com.example.demo.common.BaseEntity;
import com.example.demo.user.domain.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "notifications")
public class Notification extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Size(max = 200)
    @NotNull
    @Column(name = "title", nullable = false, length = 200)
    private String title;

    @Size(max = 500)
    @NotNull
    @Column(name = "body", nullable = false, length = 500)
    private String body;

    @Size(max = 2000)
    @Column(name = "data", length = 2000)
    private String data;

    @Size(max = 200)
    @Column(name = "click_action", length = 200)
    private String clickAction;

    @OneToMany(mappedBy = "notification")
    private Set<DeviceNotification> deviceNotifications = new LinkedHashSet<>();

}