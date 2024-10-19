package com.plcok.archive.dto;

import com.plcok.archive.entity.Archive;
import com.plcok.archive.entity.enumerated.ReactionType;
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

    private int likeCount;

    private boolean isLike;

    private LocalDateTime createdAt;

    private List<AttachResponse> archiveAttaches;

    private List<ArchiveTagResponse> tags;

    public static ArchiveResponse fromEntity(Archive archive, boolean isFollow, boolean isLike) {
        return builder()
                .id(archive.getId())
                .author(UserResponse.fromEntity(archive.getAuthor(), isFollow))
                .positionX(archive.getPositionX())
                .positionY(archive.getPositionY())
                .address(archive.getAddress())
                .name(archive.getName())
                .content(archive.getContent())
                .isPublic(archive.getIsPublic())
                .likeCount(archive.getArchiveReactions().stream().filter(ar -> ar.getReactionType() == ReactionType.LIKE).toList().size())
                .isLike(isLike)
                .createdAt(archive.getCreatedAt())
                .archiveAttaches(archive.getArchiveAttaches().stream().map(AttachResponse::fromEntity).toList())
                .tags(archive.getTags().stream().map(ArchiveTagResponse::fromEntity).toList())
                .build();
    }
}
