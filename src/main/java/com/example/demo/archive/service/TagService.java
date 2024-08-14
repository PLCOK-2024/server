package com.example.demo.archive.service;

import com.example.demo.archive.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class TagService {
    private final TagRepository tagRepository;

    @Scheduled(initialDelay = 1000, fixedDelay = 1000 * 60 * 5) // 5분마다
    public void collectTagCount() {
        log.info("start collect tag count");

        tagRepository.collectTagCount();

        log.info("done collect tag count");
    }
}
