package com.plcok.archive.service;

import com.plcok.archive.dto.ArchiveCollectResponse;
import com.plcok.archive.dto.ArchiveResponse;
import com.plcok.archive.dto.ArchiveRetrieveRequest;
import com.plcok.common.error.BusinessException;
import com.plcok.common.error.ErrorCode;
import com.plcok.common.extension.FileExtension;
import com.plcok.archive.dto.CreateArchiveRequest;
import com.plcok.archive.repository.ArchiveRepository;
import com.plcok.common.dto.PaginateResponse;
import com.plcok.archive.entity.Archive;
import com.plcok.archive.entity.ArchiveAttach;
import com.plcok.archive.entity.ArchiveTag;
import com.plcok.common.storage.IStorageManager;
import com.plcok.user.entity.Folder;
import com.plcok.user.entity.FolderArchive;
import com.plcok.user.entity.User;
import com.plcok.user.repository.FolderArchiveRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.ExtensionMethod;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@RequiredArgsConstructor
@ExtensionMethod(FileExtension.class)
public class ArchiveService {
    private final IStorageManager storageManager;
    private final ArchiveRepository archiveRepository;
    private final GeometryFactory geometryFactory;
    private final FolderArchiveRepository folderArchiveRepository;

    @Transactional(rollbackFor = IOException.class)
    public ArchiveResponse create(User author, CreateArchiveRequest request, List<MultipartFile> attaches) throws IOException {
        if (attaches == null) {
            attaches = new ArrayList<>();
        }

        // 아카이브 생성
        var archive = Archive.builder()
                .location(geometryFactory.createPoint(new Coordinate(request.getPositionX(), request.getPositionY())))
                .positionX(request.getPositionX())
                .positionY(request.getPositionY())
                .address(request.getAddress())
                .name(request.getName())
                .content(request.getContent())
                .isPublic(request.isPublic())
                .author(author)
                .build();

        archiveRepository.save(archive);

        // 첨부 생성
        List<ArchiveAttach> attachEntities = new ArrayList<>();
        for (byte i = 0; i < attaches.size(); i++) {
            MultipartFile o = attaches.get(i);
            var size = o.getImageSize();
            ArchiveAttach build = ArchiveAttach.builder()
                    .name(o.getOriginalFilename())
                    .path(storageManager.put(author.getId().toString(), o))
                    .width(size.getLeft())
                    .height(size.getRight())
                    .sequence(i)
                    .archive(archive)
                    .build();
            attachEntities.add(build);
        }
        archive.setArchiveAttaches(attachEntities);

        var index = new AtomicInteger();
        archive.setTags(
                request.getTags().stream().map(o -> ArchiveTag.builder()
                        .name(o.getName())
                        .archive(archive)
                        .sequence(index.getAndIncrement())
                        .build()
                ).toList()
        );

        return ArchiveResponse.fromEntity(archive);
    }

    @Transactional(readOnly = true)
    public ArchiveCollectResponse retrieve(User author, ArchiveRetrieveRequest request) {
        var archives = archiveRepository.retrieve(author, request);
        return ArchiveCollectResponse.builder()
                .collect(archives.stream().map(ArchiveResponse::fromEntity).toList())
                .meta(PaginateResponse.builder().count(archives.size()).build())
                .build();
    }

    public ArchiveResponse get(Archive archive) {
        return ArchiveResponse.fromEntity(archive);
    }

    public ArchiveCollectResponse getByUser(User user, User author) {
        var isAuthor = user.getId().equals(author.getId());
        List<Archive> archives;
        if (isAuthor) {
            archives = archiveRepository.getByAuthor(author);
        } else {
            archives = archiveRepository.getByAuthorAndIsPublic(author, true);
        }

        return ArchiveCollectResponse.builder()
                .collect(archives.stream().map(ArchiveResponse::fromEntity).toList())
                .meta(PaginateResponse.builder().count(archives.size()).build())
                .build();
    }

    @Transactional(readOnly = true)
    public ArchiveCollectResponse getByFolder(User user, Folder folder) {
        if (!folder.getUser().getId().equals(user.getId())) {
            throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
        List<FolderArchive> folderArchives = folderArchiveRepository.getByFolder(folder);
        return ArchiveCollectResponse.builder()
                .collect(folderArchives.stream().map((fa) -> ArchiveResponse.fromEntity(fa.getArchive())).toList())
                .meta(PaginateResponse.builder().count(folderArchives.size()).build())
                .build();
    }

    @Transactional
    public boolean changeIsPublic(User user, Archive archive) {
        if (!archive.getUser().getId().equals(user.getId())) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED);
        }
        archive.changeIsPublic();
        return archive.getIsPublic();
    }
}
