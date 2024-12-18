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
import com.plcok.user.repository.FollowerRepository;
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
    private final FollowerRepository followerRepository;

    @Override
    @Transactional
    public FolderResponse createFolder(User user, CreateFolderRequest request) {
        return FolderResponse.fromEntity(folderRepository.save(Folder.of(user, request)));
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
        boolean isFollow = followerRepository.existsByFollowerAndFollow(user, archive.getAuthor());
        if (!isFollow && !user.getId().equals(archive.getAuthor().getId())) {
            throw new BusinessException(ErrorCode.ONLY_ADDABLE_FOLLOWERS_ARCHIVE);
        }
        Optional<FolderArchive> folderArchiveOptional = folderArchiveRepository.findByFolderIdAndArchiveId(folder.getId(), archive.getId());
        if (folderArchiveOptional.isPresent()) {
            throw new BusinessException(ErrorCode.FOLDERARCHIVE_ALREADY_ADDED);
        }
        folderArchiveRepository.save(FolderArchive.of(folder, archive));
    }

    @Override
    @Transactional
    public void removeArchiveFromFolder(User user, Folder folder, Archive archive) {
        Optional<FolderArchive> folderArchiveOptional = folderArchiveRepository.findByFolderIdAndArchiveId(folder.getId(), archive.getId());
        if (folderArchiveOptional.isEmpty()) {
            throw new BusinessException(ErrorCode.FOLDERARCHIVE_ALREADY_REMOVED);
        }
        folderArchiveRepository.deleteByFolderIdAndArchiveId(folder.getId(), archive.getId());
    }

    @Override
    @Transactional
    public Boolean changeIsPublic(User user, Folder folder, boolean isPublic) {
        folder.setPublic(isPublic);
        return folder.isPublic();
    }

    @Override
    @Transactional
    public void deleteFolder(User user, Folder folder) {
        folderRepository.delete(folder);
    }
}
