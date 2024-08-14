package com.example.demo.archive.repository;

import com.example.demo.common.entity.Tag;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface TagRepository extends JpaRepository<Tag, Long> {
    @Modifying
    @Query(nativeQuery = true, value = """
insert into tags (`name`, `count`)
    select `name`, count(*) as `count` from archive_tags group by `name`
    on duplicate KEY UPDATE
    `count` = values(`count`)
""")
    @Transactional
    int collectTagCount();
}