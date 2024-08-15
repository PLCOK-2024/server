package com.example.demo.archive.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
public class CreateArchiveRequest {
    @JsonProperty("position_x")
    @NotNull
    private double positionX;

    @JsonProperty("position_y")
    @NotNull
    private double positionY;

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
}
