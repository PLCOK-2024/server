package com.plcok.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.plcok.common.dto.CollectResponse;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@SuperBuilder
@Getter
public class UserCollectResponse extends CollectResponse<UserResponse> {
    @JsonProperty("users")
    List<UserResponse> collect;
}
