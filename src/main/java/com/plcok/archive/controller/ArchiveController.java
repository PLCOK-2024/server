package com.plcok.archive.controller;

import com.plcok.archive.dto.ArchiveCollectResponse;
import com.plcok.archive.dto.ArchiveResponse;
import com.plcok.archive.dto.CreateArchiveRequest;
import com.plcok.archive.service.ArchiveService;
import com.plcok.common.argumenthandler.Entity;
import com.plcok.archive.entity.Archive;
import com.plcok.common.service.ReportService;
import com.plcok.user.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/archives")
@Tag(name = "아카이브 API")
public class ArchiveController {
    private final ArchiveService service;
    private final ReportService reportService;

    @Operation(summary = "아카이브 생성")
    @ApiResponse(responseCode = "201")
    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<ArchiveResponse> create(
            @AuthenticationPrincipal(errorOnInvalidType = true) User author,
            @RequestPart(value = "request") @Valid CreateArchiveRequest request,
            @RequestPart(required = false) List<MultipartFile> attaches
    ) throws IOException {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.create(author, request ,attaches));
    }

    @Operation(summary = "아카이브 조회")
    @ApiResponse(responseCode = "200")
    @GetMapping
    public ResponseEntity<ArchiveCollectResponse> findNearArchives(
            @AuthenticationPrincipal User user,
            @RequestParam(value = "topLeftLatitude") double topLeftLatitude,
            @RequestParam(value = "topLeftLongitude") double topLeftLongitude,
            @RequestParam(value = "bottomRightLatitude") double bottomRightLatitude,
            @RequestParam(value = "bottomRightLongitude") double bottomRightLongitude
    ) {
        return ResponseEntity.ok(service.findNearArchives(user, topLeftLatitude, topLeftLongitude, bottomRightLatitude, bottomRightLongitude));
    }
  
    @Operation(summary = "아카이브 신고")
    @ApiResponse(responseCode = "204")
    @PostMapping("{archiveId}/report")
    public ResponseEntity<ArchiveResponse> report(
            @PathVariable(name = "archiveId") long ignoredArchiveId,
            @Entity(name = "archiveId") Archive archive,
            @AuthenticationPrincipal(errorOnInvalidType = true) User user
    ) {
        reportService.report(user, archive);
        return ResponseEntity.noContent().build();
    }
}
