package com.plcok.archive.repository;

import com.plcok.archive.entity.Archive;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArchiveRepository extends JpaRepository<Archive, Long>, ArchiveRepositoryCustom {
}