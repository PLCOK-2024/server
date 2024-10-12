package com.plcok.archive.dto;

import com.plcok.archive.entity.Archive;
import com.plcok.user.dto.UserResponse;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArchiveResponse {
    private Long id;

    private UserResponse author;

    private Double positionX;

    private Double positionY;

    private String address;

    private String name;

    private String content;

    private Boolean isPublic;

    private LocalDateTime createdAt;

    private List<AttachResponse> archiveAttaches;

    private List<ArchiveTagResponse> tags;

    public static ArchiveResponse fromEntity(Archive archive) {
        return builder()
                .id(archive.getId())
                .author(UserResponse.fromEntity(archive.getAuthor()))
                .positionX(archive.getPositionX())
                .positionY(archive.getPositionY())
                .address(archive.getAddress())
                .name(archive.getName())
                .content(archive.getContent())
                .isPublic(archive.getIsPublic())
                .createdAt(archive.getCreatedAt())
                .archiveAttaches(archive.getArchiveAttaches().stream().map(AttachResponse::fromEntity).toList())
                .tags(archive.getTags().stream().map(ArchiveTagResponse::fromEntity).toList())
                .build();
    }
}
