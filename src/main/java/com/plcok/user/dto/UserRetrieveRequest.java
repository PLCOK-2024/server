package com.plcok.user.dto;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springdoc.core.annotations.ParameterObject;

@Getter
@Setter
@NoArgsConstructor
@ParameterObject
public class UserRetrieveRequest {
    @Parameter(description = "입력안함: 전체조회, true: 차단한 사람, false: 차단 안한사람")
    private Boolean block = null;

    @Parameter(description = "입력안함: 전체조회, true: 내가 팔로우한사람, false: 내가 팔로우 안한사람")
    private Boolean follow = null;

    @Parameter(description = "입력안함: 전체조회, true: 나를 팔로우한사람, false: 나를 팔로우 안한사람")
    private Boolean follower = null;

    @Parameter(description = "검색어")
    private String q;

    @Parameter(description = "조회 수")
    @Schema(defaultValue = "100")
    private Long limit = 100L;
}
