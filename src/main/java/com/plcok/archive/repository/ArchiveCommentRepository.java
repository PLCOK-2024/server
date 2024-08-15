package com.plcok.archive.repository;

import com.plcok.archive.entity.Archive;
import com.plcok.archive.entity.ArchiveComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArchiveCommentRepository extends JpaRepository<ArchiveComment, Long> {
    List<ArchiveComment> findByArchiveAndParentIsNull(@Param("archive") Archive archive);
}