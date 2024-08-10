package com.example.demo.archive.dto;

import com.example.demo.common.entity.ArchiveAttach;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AttachResponse {
    private String name;

    private String path;

    private Integer width;

    private Integer height;


    public static AttachResponse fromEntity(ArchiveAttach attach) {
        return builder()
                .name(attach.getName())
                .path(attach.getPath())
                .width(attach.getWidth())
                .height(attach.getHeight())
                .build();
    }
}
