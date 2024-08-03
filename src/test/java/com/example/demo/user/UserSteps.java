package com.example.demo.user;

import com.example.demo.user.dto.SignupRequest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;

public class UserSteps {

    public static Long signUp(SignupRequest request) {
        return RestAssured
                .given().log().all()
                .contentType(ContentType.JSON.withCharset("UTF-8"))
                .body(request)
                .when()
                .post("/api/users")
                .then().log().all()
                .statusCode(201)
                .extract().as(Long.class);
    }
}
