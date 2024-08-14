package com.example.demo.archive.dto;

import com.example.demo.common.rule.UniqueElementsBy;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
public class CreateArchiveRequest {
    @JsonProperty("position_x")
    @NotNull
    private BigDecimal positionX;

    @JsonProperty("position_y")
    @NotNull
    private BigDecimal positionY;

    @Size(max = 200)
    @NotNull
    private String address;

    @Size(max = 200)
    @NotNull
    private String name;

    @Size(max = 2000)
    @NotNull
    private String content;

    @JsonProperty("is_public")
    private boolean isPublic = true;

    @Size(max = 10)
    @UniqueElementsBy
    private List<TagRequest> tags = new ArrayList<>();
}
