package com.plcok.user.service;

import com.plcok.archive.entity.Archive;
import com.plcok.common.error.BusinessException;
import com.plcok.common.error.ErrorCode;
import com.plcok.user.dto.request.CreateFolderRequest;
import com.plcok.user.dto.response.FolderCollectResponse;
import com.plcok.user.dto.response.FolderResponse;
import com.plcok.user.entity.Folder;
import com.plcok.user.entity.FolderArchive;
import com.plcok.user.entity.User;
import com.plcok.user.repository.FolderArchiveRepository;
import com.plcok.user.repository.folder.FolderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FolderServiceImpl implements FolderService {

    private final FolderRepository folderRepository;

    private final FolderArchiveRepository folderArchiveRepository;

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
    public void addArchiveToFolder(User user, Folder folder, Archive archive) {
        Optional<FolderArchive> folderArchiveOptional = folderArchiveRepository
                .findByFolderIdAndArchiveId(folder.getId(), archive.getId());
        if (folderArchiveOptional.isPresent()) {
            throw new BusinessException(ErrorCode.FOLDERARCHIVE_ALREADY_ADDED);
        }
        FolderArchive folderArchive = FolderArchive.of(folder, archive);
        folderArchiveRepository.save(folderArchive);
    }

    @Override
    @Transactional
    public void removeArchiveFromFolder(User user, Folder folder, Archive archive) {
        Optional<FolderArchive> folderArchiveOptional = folderArchiveRepository
                .findByFolderIdAndArchiveId(folder.getId(), archive.getId());
        if (folderArchiveOptional.isEmpty()) {
            throw new BusinessException(ErrorCode.FOLDERARCHIVE_ALREADY_REMOVED);
        }
        folderArchiveRepository.deleteByFolderIdAndArchiveId(folder.getId(), archive.getId());
    }
}
