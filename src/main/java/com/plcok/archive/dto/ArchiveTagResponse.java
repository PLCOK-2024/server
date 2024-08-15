package com.plcok.archive.dto;

import com.plcok.archive.entity.ArchiveTag;
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
