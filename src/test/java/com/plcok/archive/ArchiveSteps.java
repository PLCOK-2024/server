package com.plcok.archive;

import com.plcok.archive.dto.ArchiveCollectResponse;
import com.plcok.archive.dto.ArchiveResponse;
import com.plcok.archive.dto.CreateArchiveRequest;
import io.restassured.RestAssured;
import io.restassured.builder.MultiPartSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import com.plcok.common.error.ErrorResponse;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class ArchiveSteps {

    private static ValidatableResponse createArchive(String token, CreateArchiveRequest request, InputStream... files) {
        var client = RestAssured.given();

        for (var file : files) {
            client.multiPart("attaches", "image.jpg", file, "image/jpeg");
        }

        return client
                .log().all()
                .contentType(ContentType.MULTIPART.withCharset("UTF-8"))
                .multiPart(new MultiPartSpecBuilder(request).controlName("request").mimeType("application/json").charset(StandardCharsets.UTF_8).build())
                .when()
                .auth().oauth2(token)
                .post("/api/archives")
                .then().log().all();
    }

    public static ArchiveResponse successCreateArchive(String token, CreateArchiveRequest request, InputStream... files) {
        return createArchive(token, request, files)
                .statusCode(201)
                .extract().as(ArchiveResponse.class);
    }

    public static ErrorResponse failCreateArchiveBecauseValidation(String token, CreateArchiveRequest request, InputStream... files) {
        return createArchive(token, request, files)
                .statusCode(400)
                .extract().as(ErrorResponse.class);
    }

    public static ArchiveCollectResponse findNearArchives(String token, double x, double y) {
        return RestAssured
                .given().log().all()
                .when()
                .auth().oauth2(token)
                .queryParam("x", x)
                .queryParam("y", y)
                .get("/api/archives")
                .then().log().all()
                .statusCode(200)
                .extract().as(ArchiveCollectResponse.class);
    }
}
