package com.example.demo.archive.repository;

import com.example.demo.common.entity.Archive;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArchiveRepository extends JpaRepository<Archive, Long>, ArchiveRepositoryCustom {
}