package com.plcok.archive.service;

import com.plcok.archive.dto.CommentCollectResponse;
import com.plcok.archive.dto.CommentDetailResponse;
import com.plcok.archive.dto.CommentResponse;
import com.plcok.archive.dto.CreateCommentRequest;
import com.plcok.archive.repository.ArchiveCommentRepository;
import com.plcok.common.dto.PaginateResponse;
import com.plcok.archive.entity.Archive;
import com.plcok.archive.entity.ArchiveComment;
import com.plcok.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final ArchiveCommentRepository repository;

    public CommentResponse create(User author, Archive archive, CreateCommentRequest request, ArchiveComment parent) {
        var comment = ArchiveComment.builder()
                .author(author)
                .archive(archive)
                .parent(parent)
                .content(request.getContent())
                .build();

        return CommentResponse.fromEntity(repository.save(comment));
    }

    public CommentCollectResponse get(Archive archive) {
        var comments = repository.findByArchiveAndParentIsNull(archive);
        return CommentCollectResponse.builder()
                .collect(comments.stream().map(CommentResponse::fromEntity).toList())
                .meta(PaginateResponse.builder().count(comments.size()).build())
                .build();
    }

    public CommentDetailResponse find(ArchiveComment comment) {
        return CommentDetailResponse.fromEntity(comment);
    }
}
