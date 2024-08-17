package com.plcok.archive;

import com.plcok.archive.dto.ArchiveCollectResponse;
import com.plcok.archive.dto.ArchiveResponse;
import com.plcok.archive.dto.ArchiveRetrieveRequest;
import com.plcok.archive.dto.CreateArchiveRequest;
import com.plcok.util.MockMultipartFileFixture;
import io.restassured.RestAssured;
import io.restassured.builder.MultiPartSpecBuilder;
import io.restassured.http.ContentType;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

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

    public static ArchiveCollectResponse retrieve(String token, ArchiveRetrieveRequest request) {
        return RestAssured
                .given().log().all()
                .when()
                .auth().oauth2(token)
                .queryParams(new ObjectMapper().convertValue(request, Map.class))
                .get("/api/archives")
                .then().log().all()
                .statusCode(200)
                .extract().as(ArchiveCollectResponse.class);
    }
}
