package com.example.demo.archive.dto;

import com.example.demo.common.entity.ArchiveTag;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ArchiveTagResponse {
    private String name;

    public static ArchiveTagResponse fromEntity(ArchiveTag tag) {
        return builder()
                .name(tag.getName())
                .build();
    }
}
