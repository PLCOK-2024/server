package com.plcok.archive.repository;

import com.plcok.archive.entity.Archive;
import com.plcok.archive.entity.ArchiveReaction;
import com.plcok.archive.entity.enumerated.ReactionType;
import com.plcok.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface ArchiveReactionRepository extends JpaRepository<ArchiveReaction, Long> {
    Optional<ArchiveReaction> findByAuthorAndArchive(@Param(value = "author") User author, @Param(value = "archive") Archive archive);

    List<ArchiveReaction> findByAuthorAndReactionTypeAndArchiveIdIn(@Param(value = "author") User author, @Param(value = "reactionType") ReactionType reactionType, @Param("archiveIds") Set<Long> archiveIds);

    boolean existsByAuthorAndReactionTypeAndArchive(@Param(value = "author") User author, @Param(value = "reactionType") ReactionType reactionType, @Param("archive") Archive archive);
}
