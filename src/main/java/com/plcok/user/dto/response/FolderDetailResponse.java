package com.plcok.user.dto.response;

import com.plcok.archive.dto.ArchiveResponse;
import com.plcok.user.entity.Folder;
import com.plcok.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.Map;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class FolderDetailResponse extends FolderResponse {

    private List<ArchiveResponse> archiveResponses;

    public static FolderDetailResponse fromEntity(Folder folder, User user, Map<Long, Boolean> isLikeMap) {
        return fromEntity(
                folder,
                builder().archiveResponses(folder.getFolderArchives()
                        .stream()
                        .map(fa -> ArchiveResponse.fromEntity(
                                fa.getArchive(),
                                !user.getId().equals(fa.getArchive().getId()),
                                isLikeMap.containsKey(fa.getArchive().getId())))
                        .toList()));
    }
}
