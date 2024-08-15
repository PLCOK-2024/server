package com.example.demo.archive.controller;

import com.example.demo.archive.dto.TagCollectResponse;
import com.example.demo.archive.service.TagService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/tags")
@Tag(name = "태그 API")
@RestController
@RequiredArgsConstructor
public class TagController {
    private final TagService service;

    @Operation(summary = "사용된 태그 조회")
    @GetMapping
    public ResponseEntity<TagCollectResponse> create(
            @RequestParam(required = false, name = "q") String q,
            @RequestParam(required = false, name = "limit", defaultValue = "100") int limit
    ) {
        return ResponseEntity.ok(service.get(q, limit));
    }
}
