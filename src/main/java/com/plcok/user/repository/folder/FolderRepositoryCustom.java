package com.plcok.user.repository.folder;

import com.plcok.user.dto.response.FolderResponse;

import java.util.List;

public interface FolderRepositoryCustom {
    List<FolderResponse> listFolderByUserId(long userId);
}
