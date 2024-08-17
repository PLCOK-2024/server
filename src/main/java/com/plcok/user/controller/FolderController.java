package com.plcok.user.controller;

import com.azure.core.annotation.Delete;
import com.plcok.archive.entity.Archive;
import com.plcok.common.argumenthandler.Entity;
import com.plcok.user.dto.request.CreateFolderRequest;
import com.plcok.user.dto.response.FolderCollectResponse;
import com.plcok.user.dto.response.FolderResponse;
import com.plcok.user.entity.Folder;
import com.plcok.user.entity.User;
import com.plcok.user.service.FolderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jdk.jfr.Frequency;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/folders")
@Tag(name = "폴더 API")
public class FolderController {

    private final FolderService folderService;

    @Operation(summary = "폴더 생성")
    @ApiResponse(responseCode = "201")
    @PostMapping
    public ResponseEntity<FolderResponse> createFolder(@AuthenticationPrincipal(errorOnInvalidType = true) User user,
                                                       @RequestBody @Valid CreateFolderRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(folderService.createFolder(user, request));
    }

    @Operation(summary = "폴더 리스트 조회")
    @ApiResponse(responseCode = "200")
    @GetMapping
    public ResponseEntity<FolderCollectResponse> getFolderList(@AuthenticationPrincipal(errorOnInvalidType = true) User user) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(folderService.getFolderList(user));
    }

    @Operation(summary = "아카이브 폴더에 등록")
    @PostMapping("/add/{folderId}/{archiveId}")
    public ResponseEntity<Void> addArchiveToFolder(@AuthenticationPrincipal(errorOnInvalidType = true) User user,
                                                             @PathVariable(name = "folderId") long ignoredFolderId,
                                                             @Entity(name = "folderId") Folder folder,
                                                             @PathVariable(name = "archiveId") long ignoredArchiveId,
                                                             @Entity(name = "archiveId") Archive archive) {
        folderService.addArchiveToFolder(user, folder, archive);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Operation(summary = "아카이브 폴더에서 삭제")
    @ApiResponse(responseCode = "204")
    @DeleteMapping("/remove/{folderId}/{archiveId}")
    public ResponseEntity<Void> removeArchiveFromFolder(@AuthenticationPrincipal(errorOnInvalidType = true) User user,
                                                                  @PathVariable(name = "folderId") long ignoredFolderId,
                                                                  @Entity(name = "folderId") Folder folder,
                                                                  @PathVariable(name = "archiveId") long ignoredArchiveId,
                                                                  @Entity(name = "archiveId") Archive archive) {
        folderService.removeArchiveFromFolder(user, folder, archive);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
