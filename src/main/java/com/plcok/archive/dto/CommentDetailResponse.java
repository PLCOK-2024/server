package com.plcok.archive.dto;

import com.plcok.archive.entity.ArchiveComment;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@SuperBuilder
public class CommentDetailResponse extends CommentResponse {
    List<CommentResponse> subComments;

    public static CommentDetailResponse fromEntity(ArchiveComment comment) {
        return fromEntity(
                comment,
                builder().subComments(comment.getSubComments().stream().map(CommentResponse::fromEntity).toList())
        );
    }
}
