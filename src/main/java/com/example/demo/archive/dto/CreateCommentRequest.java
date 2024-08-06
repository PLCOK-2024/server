package com.example.demo.archive.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateCommentRequest {
    @Size(min = 1, max = 200)
    private String content;
}

