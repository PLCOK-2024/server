package com.plcok.archive.service;

import com.plcok.archive.dto.ArchiveCollectResponse;
import com.plcok.archive.dto.ArchiveResponse;
import com.plcok.archive.dto.ArchiveRetrieveRequest;
import com.plcok.archive.entity.ArchiveReaction;
import com.plcok.archive.entity.enumerated.ReactionType;
import com.plcok.archive.repository.ArchiveReactionRepository;
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
import com.plcok.user.dto.response.FolderDetailResponse;
import com.plcok.user.entity.Folder;
import com.plcok.user.entity.FolderArchive;
import com.plcok.user.entity.User;
import com.plcok.user.repository.FolderArchiveRepository;
import com.plcok.user.repository.FollowerRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.ExtensionMethod;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toSet;

@Service
@RequiredArgsConstructor
@ExtensionMethod(FileExtension.class)
public class ArchiveService {
    private final IStorageManager storageManager;
    private final ArchiveRepository archiveRepository;
    private final GeometryFactory geometryFactory;
    private final FolderArchiveRepository folderArchiveRepository;
    private final ArchiveReactionRepository archiveReactionRepository;
    private final FollowerRepository followerRepository;

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

        return ArchiveResponse.fromEntity(archive, false, false);
    }

    @Transactional(readOnly = true)
    public ArchiveCollectResponse retrieve(User author, ArchiveRetrieveRequest request) {
        var archives = archiveRepository.retrieve(author, request);

        Set<Long> authorIds = new HashSet<>();
        Set<Long> archiveIds = new HashSet<>();
        archives.forEach(a -> {
            authorIds.add(a.getAuthor().getId());
            archiveIds.add(a.getId());
        });

        Map<Long, Boolean> isFollowMap = getIsFollowMap(author, authorIds);
        Map<Long, Boolean> isLikeMap = getIsLikeMap(author, archiveIds);

        return ArchiveCollectResponse.builder()
                .collect(archives
                        .stream()
                        .map(a -> ArchiveResponse.fromEntity(a, isFollowMap.containsKey(a.getAuthor().getId()), isLikeMap.containsKey(a.getId())))
                        .toList())
                .meta(PaginateResponse.builder().count(archives.size()).build())
                .build();
    }

    private Map<Long, Boolean> getIsLikeMap(User author, Set<Long> archiveIds) {
        var likes = archiveReactionRepository.findByAuthorAndReactionTypeAndArchiveIdIn(author, ReactionType.LIKE, archiveIds);
        return likes.stream().collect(Collectors.toMap(l -> l.getArchive().getId(), f -> true));
    }

    private Map<Long, Boolean> getIsFollowMap(User author, Set<Long> authorIds) {
        var follows = followerRepository.findByFollowerAndFollowIdIn(author, authorIds);
        return follows.stream().collect(Collectors.toMap(f -> f.getFollow().getId(), f -> true));
    }

    public ArchiveResponse get(User user, Archive archive) {
        var isFollow = followerRepository.existsByFollowerAndFollow(user, archive.getAuthor());
        var isLike = archiveReactionRepository.existsByAuthorAndReactionTypeAndArchive(user, ReactionType.LIKE, archive);
        return ArchiveResponse.fromEntity(archive, isFollow, isLike);
    }

    public ArchiveCollectResponse getByUser(User user, User author) {
        var isAuthor = user.getId().equals(author.getId());
        List<Archive> archives;
        if (isAuthor) {
            archives = archiveRepository.getByAuthor(author);
        } else {
            archives = archiveRepository.getByAuthorAndIsPublic(author, true);
        }

        Set<Long> archiveIds = archives.stream().map(Archive::getId).collect(toSet());
        var isFollow = followerRepository.existsByFollowerAndFollow(user, author);
        var isLikeMap = getIsLikeMap(user, archiveIds);
        return ArchiveCollectResponse.builder()
                .collect(archives.stream().map(a -> ArchiveResponse.fromEntity(a, isFollow, isLikeMap.containsKey(a.getId()))).toList())
                .meta(PaginateResponse.builder().count(archives.size()).build())
                .build();
    }

    @Transactional(readOnly = true)
    public ArchiveCollectResponse getByFolder(User user, Folder folder) {
        if (!folder.getUser().getId().equals(user.getId())) {
            throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
        List<FolderArchive> folderArchives = folderArchiveRepository.getByFolder(folder);
        Set<Long> archiveIds = folderArchives.stream().map(fa -> fa.getArchive().getId()).collect(toSet());
        var isLikeMap = getIsLikeMap(user, archiveIds);
        return ArchiveCollectResponse.builder()
                .collect(folderArchives.stream().map(fa -> ArchiveResponse
                        .fromEntity(fa.getArchive(), !user.getId().equals(fa.getArchive().getId()), isLikeMap.containsKey(fa.getArchive().getId()))).toList())
                .meta(PaginateResponse.builder().count(folderArchives.size()).build())
                .build();
    }

    @Transactional(readOnly = true)
    public FolderDetailResponse getArchivesWithFolderInfo(User user, Folder folder) {
        if (!folder.getUser().getId().equals(user.getId())) {
            throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
        Set<Long> archiveIds = folder.getFolderArchives().stream().map(fa -> fa.getArchive().getId()).collect(toSet());
        var isLikeMap = getIsLikeMap(user, archiveIds);
        return FolderDetailResponse.fromEntity(folder, user, isLikeMap);
    }

    @Transactional
    public boolean changeIsPublic(Archive archive, boolean isPublic) {
        archive.setIsPublic(isPublic);
        return archive.getIsPublic();
    }

    @Transactional
    public void deleteArchive(Archive archive) {
        archiveRepository.delete(archive);
    }

    @Transactional
    public void like(User user, Archive archive) {
        Optional<ArchiveReaction> archiveReactionOptional = archiveReactionRepository.findByAuthorAndArchive(user, archive);
        if (archiveReactionOptional.isPresent()) {
            throw new BusinessException(ErrorCode.ALREADY_LIKE_ARCHIVE);
        }
        archiveReactionRepository.save(new ArchiveReaction(archive, user, ReactionType.LIKE));
    }

    @Transactional
    public void dislike(User user, Archive archive) {
        Optional<ArchiveReaction> archiveReactionOptional = archiveReactionRepository.findByAuthorAndArchive(user, archive);
        if (archiveReactionOptional.isEmpty()) {
            throw new BusinessException(ErrorCode.ALREADY_DISLIKE_ARCHIVE);
        }
        archiveReactionRepository.delete(archiveReactionOptional.get());
    }
}
