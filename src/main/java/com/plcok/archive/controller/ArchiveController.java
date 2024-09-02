package com.plcok.archive.controller;

import com.plcok.archive.dto.ArchiveCollectResponse;
import com.plcok.archive.dto.ArchiveResponse;
import com.plcok.archive.dto.ArchiveRetrieveRequest;
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
@RequestMapping("/api")
@Tag(name = "아카이브 API")
public class ArchiveController {
    private final ArchiveService service;
    private final ReportService reportService;

    @Operation(summary = "아카이브 생성")
    @ApiResponse(responseCode = "201")
    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE}, path = "archives")
    public ResponseEntity<ArchiveResponse> create(
            @AuthenticationPrincipal(errorOnInvalidType = true) User author,
            @RequestPart(value = "request", name = "request") @Valid CreateArchiveRequest request,
            @RequestPart(required = false, name = "attaches") List<MultipartFile> attaches
    ) throws IOException {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.create(author, request ,attaches));
    }

    @Operation(summary = "아카이브 조회")
    @ApiResponse(responseCode = "200")
    @GetMapping(path = "archives")
    public ResponseEntity<ArchiveCollectResponse> retrieve(
            @AuthenticationPrincipal User user,
            @ModelAttribute ArchiveRetrieveRequest request
    ) {
        return ResponseEntity.ok(service.retrieve(user, request));
    }

    @Operation(summary = "ID로 아카이브 조회")
    @ApiResponse(responseCode = "200")
    @GetMapping(path = "archives/{archiveId}")
    public ResponseEntity<ArchiveResponse> retrieve(
            @AuthenticationPrincipal User ignoredUser,
            @Entity(name = "archiveId") Archive archive,
            @PathVariable(name = "archiveId") String ignoredAuthorId
    ) {
        return ResponseEntity.ok(service.get(archive));
    }

    @Operation(summary = "작성자로 아카이브 조회")
    @ApiResponse(responseCode = "200")
    @GetMapping(path = "users/{authorId}/archives")
    public ResponseEntity<ArchiveCollectResponse> getByAuthor(
            @AuthenticationPrincipal User user,
            @Entity(name = "authorId") User author,
            @PathVariable(name = "authorId") String ignoredAuthorId
    ) {
        return ResponseEntity.ok(service.getByUser(user, author));
    }
  
    @Operation(summary = "아카이브 신고")
    @ApiResponse(responseCode = "204")
    @PostMapping("archives/{archiveId}/report")
    public ResponseEntity<ArchiveResponse> report(
            @PathVariable(name = "archiveId") long ignoredArchiveId,
            @Entity(name = "archiveId") Archive archive,
            @AuthenticationPrincipal(errorOnInvalidType = true) User user
    ) {
        reportService.report(user, archive);
        return ResponseEntity.noContent().build();
    }
}
