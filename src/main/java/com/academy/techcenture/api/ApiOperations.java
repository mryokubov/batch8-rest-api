package com.academy.techcenture.api;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import static io.restassured.RestAssured.given;

public class ApiOperations {

    private ApiOperations(){}

    private static final String AUTH_TOKEN = "Bearer bf6565d43d43d1a69a735e6327eae3fa557562dff56343cafb3f68284d52e547";

    public static Response performGetRequest(String path, boolean requiresAuth){
        RequestSpecification requestSpecification = given();
        if (requiresAuth){
            requestSpecification = requestSpecification.header("Authorization", AUTH_TOKEN);
        }
        return requestSpecification
                .when()
                .get(path);
    }

    public static Response performGetRequestQueryParam(String path, String param, String value, boolean requiresAuth){
        RequestSpecification requestSpecification = given();
        if (requiresAuth){
            requestSpecification = requestSpecification.header("Authorization", AUTH_TOKEN);
        }
        return   requestSpecification
                .queryParam(param, value)
                .when()
                .get(path);
    }

    public static Response performGetRequestPathParam(String path, String param, String value, boolean requiresAuth){
        RequestSpecification requestSpecification = given();
        if (requiresAuth){
            requestSpecification = requestSpecification.header("Authorization", AUTH_TOKEN);
        }
        return   requestSpecification
                .pathParams(param, value)
                .when()
                .get(path);
    }

    public static Response performPostRequest(String path, Object payload){
        return  given()
                .contentType(ContentType.JSON)
                .header("Authorization", AUTH_TOKEN)
                .body(payload)
                .when()
                .post(path);
    }

    public static Response performPatchRequest(String path, String param, String value, Object payload){
        return   given()
                .contentType(ContentType.JSON)
                .header("Authorization", AUTH_TOKEN)
                .body(payload)
                .pathParams(param, value)
                .when()
                .patch(path);
    }

    public static Response performDeleteRequest(String path, String param, String value){
        return given()
                .header("Authorization", AUTH_TOKEN)
                .pathParams(param, value)
                .when()
                .delete(path);
    }
}
