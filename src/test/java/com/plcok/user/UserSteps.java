package com.plcok.user;

import com.plcok.user.dto.request.LoginRequest;
import com.plcok.user.dto.request.SignupRequest;
import com.plcok.user.dto.response.TokenResponse;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;

public class UserSteps {

    public static Long signUp(SignupRequest request) {
        return RestAssured
                .given().log().all()
                .contentType(ContentType.JSON.withCharset("UTF-8"))
                .body(request)
                .when()
                .post("/api/auth/signUp")
                .then().log().all()
                .statusCode(201)
                .extract().as(Long.class);
    }

    public static TokenResponse testLogin(LoginRequest request) {
        return RestAssured
                .given().log().all()
                .contentType(ContentType.JSON.withCharset("UTF-8"))
                .body(request)
                .when()
                .post("/api/auth/testLogin")
                .then().log().all()
                .statusCode(200)
                .extract().as(TokenResponse.class);
    }

    public static void block(String token, Long userId) {
        RestAssured
            .given().log().all()
            .contentType(ContentType.JSON.withCharset("UTF-8"))
            .when()
            .auth().oauth2(token)
            .post("/api/users/{userId}/block", userId)
            .then().log().all()
            .statusCode(204);
    }
}
