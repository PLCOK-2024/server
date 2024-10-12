package com.plcok.user.service;

import com.plcok.archive.entity.Archive;
import com.plcok.user.dto.request.CreateFolderRequest;
import com.plcok.user.dto.response.FolderCollectResponse;
import com.plcok.user.dto.response.FolderResponse;
import com.plcok.user.entity.Folder;
import com.plcok.user.entity.User;

public interface FolderService {

    FolderResponse createFolder(User user, CreateFolderRequest request);

    FolderCollectResponse getFolderList(User user);

    void addArchiveToFolder(User user, Folder folder, Archive archive);

    void removeArchiveFromFolder(User user, Folder folder, Archive archive);

    Boolean changeIsPublic(User user, Folder folder, boolean isPublic);
}
