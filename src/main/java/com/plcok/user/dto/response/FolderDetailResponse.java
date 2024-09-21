package com.plcok.user.dto.response;

import com.plcok.archive.dto.ArchiveResponse;
import com.plcok.user.entity.Folder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class FolderDetailResponse extends FolderResponse {

    private List<ArchiveResponse> archiveResponses;

    public static FolderDetailResponse fromEntity(Folder folder) {
        return fromEntity(
                folder,
                builder().archiveResponses(folder.getFolderArchives().stream().map(fa -> ArchiveResponse.fromEntity(fa.getArchive())).toList()));
    }
}
