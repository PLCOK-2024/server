package com.plcok.archive.dto;

import com.plcok.archive.entity.ArchiveAttach;
import lombok.*;
import org.springframework.beans.factory.annotation.Value;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
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
