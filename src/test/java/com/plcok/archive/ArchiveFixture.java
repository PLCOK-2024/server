package com.plcok.archive;

import com.plcok.archive.dto.CreateArchiveRequest;
import com.plcok.archive.dto.TagRequest;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ArchiveFixture {
    @AllArgsConstructor
    public static class Coordinate {
        double topLeftLatitude;
        double topLeftLongitude;
        double bottomRightLatitude;
        double bottomRightLongitude;

        public static Coordinate coordinateIncludingSongpa() {
            return new Coordinate(37.5202, 127.0787, 37.5015, 127.0961);
        }
    }

    public static CreateArchiveRequest defaultCreateArchiveRequest() {
        return CreateArchiveRequest.builder()
                .name("킨더커피")
                .content("킨더커피 크렘브륄레 마카롱 맛있어요")
                .address("서울특별시 송파구 석촌호수로 135")
                .positionX(37.5152)
                .positionY(127.0837)
                .tags(List.of(new TagRequest("마카롱")))
                .isPublic(true)
                .build();
    }

    public static CreateArchiveRequest inSongpaCreateArchiveRequest() {
        return CreateArchiveRequest.builder()
                .name("콰이어트크림티")
                .content("스콘과 차가 맛있어요")
                .address("서울특별시 송파구 삼전로12길 7")
                .positionX(37.5065)
                .positionY(127.0911)
                .tags(List.of(new TagRequest("스콘")))
                .isPublic(true)
                .build();
    }

    public static CreateArchiveRequest inYoungdeungpoCreateArchiveRequest() {
        return CreateArchiveRequest.builder()
                .name("뱀부그로브 커피")
                .content("버터바가 맛있어요")
                .address("서울특별시 영등포구 영신로 166 1층 121호")
                .positionX(37.5227)
                .positionY(126.9022)
                .tags(List.of(new TagRequest("버터바")))
                .isPublic(true)
                .build();
    }
}
