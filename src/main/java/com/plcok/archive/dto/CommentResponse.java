package com.plcok.archive.dto;

import com.plcok.archive.entity.ArchiveComment;
import com.plcok.user.dto.UserResponse;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@SuperBuilder
public class CommentResponse {
    private long id;

    private String content;

    private UserResponse author;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public static CommentResponse fromEntity(ArchiveComment comment) {
        return fromEntity(comment, builder());
    }

    public static <T extends CommentResponse> T fromEntity(ArchiveComment comment, CommentResponse.CommentResponseBuilder<T, ?> builder) {
        return builder
                .id(comment.getId())
                .content(comment.getContent())
                .author(UserResponse.fromEntity(comment.getUser()))
                .createdAt(comment.getCreatedAt())
                .updatedAt(comment.getUpdatedAt())
                .build();
    }
}