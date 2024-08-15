package com.plcok.archive.dto;

import com.plcok.common.dto.CollectResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@SuperBuilder
@Getter
public class TagCollectResponse extends CollectResponse<TagResponse> {
    @JsonProperty("tags")
    List<TagResponse> collect;
}
