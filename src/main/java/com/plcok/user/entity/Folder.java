package com.plcok.user.entity;

import com.plcok.common.BaseEntity;
import com.plcok.user.dto.request.CreateFolderRequest;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

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

    @NotNull
    @Column(name = "is_public", nullable = false)
    @Setter
    private boolean isPublic = false;

    @OneToMany(mappedBy = "folder")
    private List<FolderArchive> folderArchives;

    public static Folder of(User user, CreateFolderRequest request) {
        return builder()
                .user(user)
                .name(request.getName())
                .isPublic(request.isPublic())
                .folderArchives(new ArrayList<>())
                .build();
    }
}