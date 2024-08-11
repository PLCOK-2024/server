package com.example.demo.archive;

import com.example.demo.archive.dto.ArchiveResponse;
import com.example.demo.archive.dto.CreateArchiveRequest;
import com.example.demo.util.MockMultipartFileFixture;
import io.restassured.RestAssured;
import io.restassured.builder.MultiPartSpecBuilder;
import io.restassured.http.ContentType;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class ArchiveSteps {
    public static ArchiveResponse createArchive(String token, CreateArchiveRequest request) throws IOException {
        return RestAssured
                .given().log().all()
                .contentType(ContentType.MULTIPART.withCharset("UTF-8"))
                .multiPart(new MultiPartSpecBuilder(request).controlName("request").mimeType("application/json").charset(StandardCharsets.UTF_8).build())
                .multiPart("ticketImage", MockMultipartFileFixture.mockImageFile(), "image/jpeg")
                .when()
                .auth().oauth2(token)
                .post("/api/archives")
                .then().log().all()
                .statusCode(201)
                .extract().as(ArchiveResponse.class);
    }

    public static List<ArchiveResponse> findNearArchives(String token, double x, double y) {
        return RestAssured
                .given().log().all()
                .when()
                .auth().oauth2(token)
                .get("/api/archives/{x}/{y}", x, y)
                .then().log().all()
                .statusCode(200)
                .extract().body().jsonPath().getList(".", ArchiveResponse.class);
    }
}
