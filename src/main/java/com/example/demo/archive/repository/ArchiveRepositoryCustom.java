package com.example.demo.archive.repository;

import com.example.demo.common.entity.Archive;

import java.util.List;

public interface ArchiveRepositoryCustom {
    List<Archive> findNearArchives(double currentX, double currentY);
}
