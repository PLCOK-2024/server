package com.plcok.common;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/help")
@Tag(name = "HELP API")
public class HelpController {

    @Operation(summary = "에러 알림 테스트")
    @GetMapping
    public ResponseEntity<Long> signUp() throws Exception {
        throw new Exception("알림 테스트");
    }
}