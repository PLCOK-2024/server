package com.plcok.user.entity;

import com.plcok.common.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Table(name = "folders")
@Getter
@Entity
@SuperBuilder
@AttributeOverrides({
        @AttributeOverride(name = "createdAt", column = @Column(name = "created_at", nullable = false))
})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Folder extends BaseEntity {

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @NotNull
    private String name;

    private int count;

    public static Folder of(User user, String name) {
        return builder()
                .user(user)
                .name(name)
                .build();
    }

    public void plusCount() {
        count += 1;
    }

    public void minusCount() {
        count -= 1;
    }
}


