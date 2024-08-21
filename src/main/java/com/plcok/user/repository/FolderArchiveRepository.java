package com.plcok.user.repository;

import com.plcok.user.entity.FolderArchive;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FolderArchiveRepository extends JpaRepository<FolderArchive, Long> {

    Optional<FolderArchive> findByFolderIdAndArchiveId(long folderId, long archiveId);

    void deleteByFolderIdAndArchiveId(long folderId, long archiveId);
}
