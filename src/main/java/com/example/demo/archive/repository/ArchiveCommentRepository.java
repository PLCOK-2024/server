package com.example.demo.archive.repository;

import com.example.demo.common.entity.Archive;
import com.example.demo.common.entity.ArchiveComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArchiveCommentRepository extends JpaRepository<ArchiveComment, Long> {
    @Query("SELECT c from ArchiveComment c WHERE c.archive = :archive AND c.parent IS NULL")
    List<ArchiveComment> getByArchive(@Param("archive") Archive archive);

    @Query("SELECT c from ArchiveComment c WHERE c.archive = :archive AND c.parent = :parent")
    List<ArchiveComment> getByArchive(@Param("archive") Archive archive, @Param("parent") ArchiveComment parent);
}