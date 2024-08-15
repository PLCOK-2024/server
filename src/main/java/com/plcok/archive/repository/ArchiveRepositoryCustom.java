package com.plcok.archive.repository;

import com.plcok.archive.entity.Archive;

import java.util.List;

public interface ArchiveRepositoryCustom {
    List<Archive> findNearArchives(double currentX, double currentY);
}
