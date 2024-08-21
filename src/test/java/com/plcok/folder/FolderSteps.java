package com.plcok.folder;

import com.plcok.user.dto.request.CreateFolderRequest;
import com.plcok.user.dto.response.FolderCollectResponse;
import com.plcok.user.dto.response.FolderResponse;
import com.plcok.util.MockMultipartFileFixture;
import io.restassured.RestAssured;
import io.restassured.builder.MultiPartSpecBuilder;
import io.restassured.http.ContentType;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class FolderSteps {
    public static FolderResponse createFolder(String token, CreateFolderRequest request) {
        return RestAssured
                .given().log().all()
                .contentType(ContentType.JSON.withCharset("UTF-8"))
                .body(request)
                .when()
                .auth().oauth2(token)
                .post("/api/folders")
                .then().log().all()
                .statusCode(201)
                .extract().as(FolderResponse.class);
    }

    public static FolderCollectResponse getFolderList(String token) {
        return RestAssured
                .given().log().all()
                .when()
                .auth().oauth2(token)
                .get("/api/folders")
                .then().log().all()
                .statusCode(200)
                .extract().as(FolderCollectResponse.class);
    }

    public static void addArchiveToFolder(String token, long folderId, long archiveId) {
         RestAssured
            .given().log().all()
            .contentType(ContentType.JSON.withCharset("UTF-8"))
            .when()
            .auth().oauth2(token)
            .post("/api/folders/{folderId}/archives/{archiveId}", folderId, archiveId)
            .then().log().all()
            .statusCode(204)
            .extract();
    }

    public static void removeArchiveToFolder(String token, long folderId, long archiveId) {
        RestAssured
            .given().log().all()
            .contentType(ContentType.JSON.withCharset("UTF-8"))
            .when()
            .auth().oauth2(token)
            .delete("/api/folders/{folderId}/archives/{archiveId}", folderId, archiveId)
            .then().log().all()
            .statusCode(204)
            .extract();
    }
}
