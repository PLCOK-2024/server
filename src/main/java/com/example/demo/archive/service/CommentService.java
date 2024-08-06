package com.example.demo.archive.service;

import com.example.demo.archive.dto.CommentDetailResponse;
import com.example.demo.archive.dto.CommentResponse;
import com.example.demo.archive.dto.CreateCommentRequest;
import com.example.demo.archive.repository.ArchiveCommentRepository;
import com.example.demo.common.entity.Archive;
import com.example.demo.common.entity.ArchiveComment;
import com.example.demo.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final ArchiveCommentRepository repository;

    public CommentResponse create(User author, Archive archive, CreateCommentRequest request) {
        return null;
    }

    public List<CommentResponse> get(Archive archive) {
        return repository.getByArchive(archive).stream().map(CommentResponse::fromEntity).toList();
    }

    public CommentDetailResponse find(Archive archive, ArchiveComment comment) {
        return CommentDetailResponse.fromEntity(comment);
    }
}
