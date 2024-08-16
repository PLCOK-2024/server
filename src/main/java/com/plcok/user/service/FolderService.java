package com.plcok.user.service;

import com.plcok.user.dto.request.CreateFolderRequest;
import com.plcok.user.dto.response.FolderCollectResponse;
import com.plcok.user.dto.response.FolderResponse;
import com.plcok.user.entity.User;

public interface FolderService {

    FolderResponse createFolder(User user, CreateFolderRequest request);

    FolderCollectResponse getFolderList(User user);
}
