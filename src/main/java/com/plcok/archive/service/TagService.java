package com.plcok.archive.service;

import com.plcok.common.extension.ListExtension;
import com.plcok.archive.dto.TagCollectResponse;
import com.plcok.archive.dto.TagResponse;
import com.plcok.archive.repository.TagRepository;
import com.plcok.common.dto.PaginateResponse;
import com.plcok.archive.entity.Tag;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import lombok.experimental.ExtensionMethod;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Limit;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@ExtensionMethod(ListExtension.class)
public class TagService {
    private final TagRepository repository;

    @Scheduled(initialDelay = 1000, fixedDelay = 1000 * 60 * 5) // 5분마다
    public void collectTagCount() {
        log.info("start collect tag count");

        repository.collectTagCount();

        log.info("done collect tag count");
    }

    public TagCollectResponse get(@Nullable String q, int limit) {
        List<Tag> tags;
        if (q == null) {
            tags = repository.findAllByOrderByCountDesc(Limit.of(limit));
        } else {
            tags = repository.findByNameContainingOrderByCountDesc(q, Limit.of(limit));
            // 정확히 일치하는 태그는 최상단으로 이동
            var eq = tags.removeFirst(o -> o.getName().equals(q));
            if (eq != null) {
                tags.add(0, tags.removeFirst(o -> o.getName().equals(q)));
            }
        }

        return TagCollectResponse.builder()
                .collect(tags.stream().map(TagResponse::fromEntity).toList())
                .meta(PaginateResponse.builder().count(tags.size()).build())
                .build();
    }
}
