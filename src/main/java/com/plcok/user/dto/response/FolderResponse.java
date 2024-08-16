package com.plcok.user.dto.response;

import com.plcok.user.entity.Folder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FolderResponse {

    private Long id;

    private String name;

    private int count;

    public static FolderResponse from(Folder folder) {
        return builder()
                .id(folder.getId())
                .name(folder.getName())
                .count(folder.getCount())
                .build();
    }
}
