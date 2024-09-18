package com.plcok.user.dto.response;

import com.plcok.archive.dto.ArchiveResponse;
import com.plcok.user.entity.Folder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FolderResponse {

    private Long id;

    private String name;

    private int count;

    private List<ArchiveResponse> archiveResponses;

    public static FolderResponse from(Folder folder) {
        return builder()
                .id(folder.getId())
                .name(folder.getName())
                .count(folder.getFolderArchives().size())
                .archiveResponses(folder.getFolderArchives()
                        .stream()
                        .map(fa -> ArchiveResponse.fromEntity(fa.getArchive()))
                        .toList())
                .build();
    }
}
