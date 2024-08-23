package com.plcok.folder;

import com.plcok.user.dto.request.CreateFolderRequest;

public class FolderFixture {

    public static CreateFolderRequest defaultCreateFolderRequest(int i) {
        return CreateFolderRequest.builder()
                .name("folder" + i)
                .build();
    }
}
