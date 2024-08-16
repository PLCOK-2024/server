package com.plcok.user.entity;

import com.plcok.common.BaseEntity;
import com.plcok.common.entity.IReportable;
import com.plcok.common.entity.enumerated.ResourceType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Entity
@Table(name = "users")
@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseEntity implements UserDetails, IReportable {
    private String email;

    private String name;

    private String password;

    private String profileImage;

    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "ROLE", nullable = false)
    @Setter
    @Builder.Default
    private RoleType role = RoleType.USER;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<String> roles = new ArrayList<>();
        roles.add("ROLE_" + getRole());

        return roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(toList());
    }

    @Override
    public String getUsername() {
        return null;
    }

    @Override
    public User getUser() {
        return this;
    }

    @Override
    public ResourceType getResourceType() {
        return ResourceType.USER;
    }
}
