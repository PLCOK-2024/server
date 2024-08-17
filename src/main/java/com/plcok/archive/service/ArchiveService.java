package com.plcok.archive.service;

import com.plcok.archive.dto.ArchiveCollectResponse;
import com.plcok.archive.dto.ArchiveResponse;
import com.plcok.common.extension.FileExtension;
import com.plcok.archive.dto.CreateArchiveRequest;
import com.plcok.archive.repository.ArchiveRepository;
import com.plcok.common.dto.PaginateResponse;
import com.plcok.archive.entity.Archive;
import com.plcok.archive.entity.ArchiveAttach;
import com.plcok.archive.entity.ArchiveTag;
import com.plcok.common.storage.IStorageManager;
import com.plcok.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.experimental.ExtensionMethod;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
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

    @Transactional(rollbackFor = IOException.class)
    public ArchiveResponse create(User author, CreateArchiveRequest request, List<MultipartFile> attaches) throws IOException {
        if (attaches == null) {
            attaches = new ArrayList<>();
        }

        Point location = generateLocation(request.getPositionX(), request.getPositionY());

        // 아카이브 생성
        var archive = Archive.builder()
                .location(location)
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
    public ArchiveCollectResponse findNearArchives(User author, double topLeftLatitude, double topLeftLongitude, double bottomRightLatitude, double bottomRightLongitude) {
        var archives = archiveRepository.findNearArchives(author, topLeftLatitude, topLeftLongitude, bottomRightLatitude, bottomRightLongitude);
        return ArchiveCollectResponse.builder()
                .collect(archives.stream().map(ArchiveResponse::fromEntity).toList())
                .meta(PaginateResponse.builder().count(archives.size()).build())
                .build();
    }

    private Point generateLocation(double x, double y) {
        String pointWKT = String.format("POINT(%s %s)", y, x);
        Point location = null;
        try {
            location = (Point) new WKTReader().read(pointWKT);
        } catch (ParseException ex) {
            return null;
        }
        return location;
    }
}
