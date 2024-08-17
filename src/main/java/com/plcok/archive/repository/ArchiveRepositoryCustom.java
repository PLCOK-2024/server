package com.plcok.archive.repository;

import com.plcok.archive.dto.ArchiveRetrieveRequest;
import com.plcok.archive.entity.Archive;
import com.plcok.user.entity.User;

import java.util.List;

public interface ArchiveRepositoryCustom {
    List<Archive> retrieve(User author, ArchiveRetrieveRequest request);
}
