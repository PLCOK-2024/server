package com.example.demo.archive;

import com.example.demo.archive.dto.CreateArchiveRequest;

import java.math.BigDecimal;

public class ArchiveFixture {

    public static CreateArchiveRequest defaultCreateArchiveRequest() {
        return CreateArchiveRequest.builder()
                .name("이름")
                .content("킨더커피 크렘브륄레 마카롱 맛있어요")
                .address("서울특별시 송파구 석촌호수로 135")
                .positionX(BigDecimal.valueOf(37.5152))
                .positionY(BigDecimal.valueOf(127.0837))
                .isPublic(true)
                .build();
    }
}
