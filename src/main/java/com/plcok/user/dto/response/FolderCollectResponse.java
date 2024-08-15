package com.plcok.user.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.plcok.common.dto.PaginateResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FolderCollectResponse {

    private List<FolderResponse> folders;
}
