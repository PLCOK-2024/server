package com.plcok.archive.repository;

import com.plcok.archive.entity.Archive;
import com.plcok.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArchiveRepository extends JpaRepository<Archive, Long>, ArchiveRepositoryCustom {
    List<Archive> getByAuthorAndIsPublic(User author, boolean isPublic);

    List<Archive> getByAuthor(User author);
}