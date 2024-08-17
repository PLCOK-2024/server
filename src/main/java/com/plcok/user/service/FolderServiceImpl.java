package com.plcok.user.service;

import com.plcok.archive.entity.Archive;
import com.plcok.archive.repository.ArchiveRepository;
import com.plcok.common.error.BusinessException;
import com.plcok.common.error.EntityNotFoundException;
import com.plcok.common.error.ErrorCode;
import com.plcok.user.dto.request.CreateFolderRequest;
import com.plcok.user.dto.response.FolderCollectResponse;
import com.plcok.user.dto.response.FolderResponse;
import com.plcok.user.entity.Folder;
import com.plcok.user.entity.User;
import com.plcok.user.repository.folder.FolderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FolderServiceImpl implements FolderService {

    private final FolderRepository folderRepository;

    private final ArchiveRepository archiveRepository;

    @Override
    @Transactional
    public FolderResponse createFolder(User user, CreateFolderRequest request) {
        return FolderResponse.from(folderRepository.save(Folder.of(user, request.getName())));
    }

    @Override
    @Transactional(readOnly = true)
    public FolderCollectResponse getFolderList(User user) {
        return FolderCollectResponse.builder()
                .folders(folderRepository.listFolderByUserId(user.getId()))
                .build();
    }

    @Override
    @Transactional
    public FolderResponse addArchiveToFolder(User user, Folder folder, Archive archive) {
        archive.setFolder(folder);
        folder.plusCount();
        return FolderResponse.from(folder);
    }

    @Override
    @Transactional
    public FolderResponse removeArchiveFromFolder(User user, Folder folder, Archive archive) {
        archive.setFolder(null);
        folder.minusCount();
        return FolderResponse.from(folder);
    }
}
