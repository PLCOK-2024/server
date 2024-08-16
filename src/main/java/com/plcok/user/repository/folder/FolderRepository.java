package com.plcok.user.repository.folder;

import com.plcok.user.entity.Folder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FolderRepository extends JpaRepository<Folder, Long>, FolderRepositoryCustom {
}
