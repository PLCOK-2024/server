package com.plcok.archive.repository;

import com.plcok.archive.entity.Archive;
import com.plcok.archive.entity.ArchiveReaction;
import com.plcok.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ArchiveReactionRepository extends JpaRepository<ArchiveReaction, Long> {
    Optional<ArchiveReaction> findByAuthorAndArchive(@Param(value = "author") User author, @Param(value = "archive") Archive archive);
}
