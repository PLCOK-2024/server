package com.plcok.archive.dto;

import com.plcok.archive.entity.Tag;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TagResponse {
    private String name;

    private int count;

    public static TagResponse fromEntity(Tag tag) {
        return builder()
                .name(tag.getName())
                .count(tag.getCount())
                .build();
    }
}
