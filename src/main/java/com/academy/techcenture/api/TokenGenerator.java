package com.academy.techcenture.api;

import com.academy.techcenture.end_points.ApiEndPoints;
import com.github.javafaker.Faker;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class TokenGenerator {

    public static String getNewToken() {
        String[] email = {"@gmail.com", "@yahoo.com", "@hotmail.com"};
        Faker faker = new Faker();
        String clientName = faker.name().fullName();
        String clientEmail = clientName.toLowerCase().replace(" ", "") + email[(int)(Math.random() * email.length)];
        String payload =
                "{\n" +
                        "  \"clientName\": \"" + clientName + "\",\n" +
                        "  \"clientEmail\": \"" + clientEmail + "\"\n" +
                        "}";

        Response response = given()
                .contentType(ContentType.JSON)
                .body(payload)
                .post(ApiEndPoints.BASE_URL + "/api-clients")
                .then()
                .statusCode(201)
                .extract()
                .response();

        return  response
                .jsonPath()
                .getString("accessToken");
    }

    public static void main(String[] args) {

        System.out.println(getNewToken());
    }

}
