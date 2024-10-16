package com.plcok.user.dto.response;

import com.plcok.user.entity.Folder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class FolderResponse {

    private Long id;

    private String name;

    private boolean isPublic;

    private int count;

    public static FolderResponse fromEntity(Folder folder) {
        return fromEntity(folder, builder());
    }

    public static <T extends FolderResponse> T fromEntity(Folder folder, FolderResponse.FolderResponseBuilder<T, ?> builder) {
        return builder
                .id(folder.getId())
                .name(folder.getName())
                .isPublic(folder.isPublic())
                .count(folder.getFolderArchives().size())
                .build();
    }
}
