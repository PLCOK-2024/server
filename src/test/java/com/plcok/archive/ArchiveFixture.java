package com.plcok.archive;

import com.plcok.archive.dto.ArchiveRetrieveRequest;
import com.plcok.archive.dto.CreateArchiveRequest;
import com.plcok.archive.dto.TagRequest;
import java.util.List;

public class ArchiveFixture {
    public static ArchiveRetrieveRequest defaultRetrieveRequest() {
        return new ArchiveRetrieveRequest(37.5202, 127.0787, 37.5015, 127.0961, null, null);
    }

    public static CreateArchiveRequest defaultCreateArchiveRequest() {
        return CreateArchiveRequest.builder()
                .name("킨더커피")
                .content("킨더커피 크렘브륄레 마카롱 맛있어요")
                .address("서울특별시 송파구 석촌호수로 135")
                .positionX(127.0837)
                .positionY(37.5152)
                .tags(List.of(new TagRequest("마카롱")))
                .isPublic(true)
                .build();
    }

    public static CreateArchiveRequest inSongpaCreateArchiveRequest() {
        return CreateArchiveRequest.builder()
                .name("콰이어트크림티")
                .content("스콘과 차가 맛있어요")
                .address("서울특별시 송파구 삼전로12길 7")
                .positionX(127.0911)
                .positionY(37.5065)
                .tags(List.of(new TagRequest("스콘")))
                .isPublic(true)
                .build();
    }

    public static CreateArchiveRequest inYoungdeungpoCreateArchiveRequest() {
        return CreateArchiveRequest.builder()
                .name("뱀부그로브 커피")
                .content("버터바가 맛있어요")
                .address("서울특별시 영등포구 영신로 166 1층 121호")
                .positionX(126.9022)
                .positionY(37.5227)
                .tags(List.of(new TagRequest("버터바")))
                .isPublic(true)
                .build();
    }
}
