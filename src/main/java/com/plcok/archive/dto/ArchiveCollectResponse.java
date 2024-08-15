package com.plcok.archive.dto;

import com.plcok.common.dto.CollectResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@SuperBuilder
@NoArgsConstructor
public class ArchiveCollectResponse extends CollectResponse<ArchiveResponse> {
    @JsonProperty("archives")
    private List<ArchiveResponse> collect;
}
