package com.academy.techcenture.tests;

import com.academy.techcenture.pojos.Book;
import com.academy.techcenture.pojos.OrderRequestPayload;
import com.academy.techcenture.pojos.OrderResponsePayload;
import com.academy.techcenture.pojos.OrderUpdateRequestPayload;
import com.github.javafaker.Faker;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class SimpleBookApiTest {

    private static final String BASE_URL = "https://simple-books-api.glitch.me";
    private Faker faker = new Faker();


    @BeforeClass
    public static void setUp(){
        RestAssured.baseURI = BASE_URL;
    }


    @Test
    public void getApiStatusTest(){
        given()
                .when()
                .get("/status")
                .then()
                .statusCode(200)
                .body("status", equalTo("OK"));
    }


    @Test
    public void getAllBooksTest(){
        given()
                .when()
                .get("/books")
                .then()
                .statusCode(200)
                .body("size()", equalTo(6));
    }

    @Test
    public void getAllFictionBooksTest(){
        given()
                .queryParam("type", "fiction")
                .when()
                .get("/books")
                .then()
                .statusCode(200)
                .body("size()", equalTo(4));
    }

    @Test
    public void getAllNonFictionBooksTest(){
        given()
                .queryParam("type", "non-fiction")
                .when()
                .get("/books")
                .then()
                .statusCode(200)
                .body("size()", equalTo(2));
    }

    @Test
    public void getAllBooksWithLimitTest(){
        given()
                .queryParam("limit", "2")
                .when()
                .get("/books")
                .then()
                .statusCode(200)
                .body("size()", equalTo(2));
    }

    @Test
    public void getOneRandomBookTest(){
        int bookId = (int)(Math.random() * 6) + 1;
        given()
                .pathParams("bookId", bookId)
                .when()
                .get("/books/{bookId}")
                .then()
                .statusCode(200)
                .body("id", equalTo(bookId));
    }

    @Test
    public void getOneBookTest(){
        int bookId = 1;
        Response response = given()
                .pathParams("bookId", bookId)
                .when()
                .get("/books/{bookId}")
                .then()
                .statusCode(200)
                .extract()
                .response();

        Book bookResponse = response.as(Book.class);
        Assert.assertEquals(1, bookResponse.getId());
        Assert.assertEquals("The Russian", bookResponse.getName());
        Assert.assertEquals("James Patterson and James O. Born", bookResponse.getAuthor());
        Assert.assertEquals("1780899475", bookResponse.getIsbn());
        Assert.assertEquals("fiction", bookResponse.getType());
        Assert.assertEquals(12.98, bookResponse.getPrice(), 0.01);
        Assert.assertEquals(12, bookResponse.getCurrentStock());
        Assert.assertEquals(true, bookResponse.isAvailable());

    }

    @Test
    public void submitBookOrderBadTest(){

        String customerName = faker.name().fullName();
        int bookId = 1;

        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer 6ee232327ce7f4d7ecd3e44e1bd57b55df722479f8701bff5cb4d5722ceab093")
                .body("{\n" +
                        "  \"bookId\": "+bookId+",\n" +
                        "  \"customerName\": \""+customerName+"\"" +
                        "}")
                .when()
                .post("/orders")
                .then()
                .statusCode(201)
                .body("created", equalTo(true))
                .body("orderId", notNullValue());

    }

    @Test
    public void submitBookOrderTest(){

        String customerName = faker.name().fullName();
        int bookId = 3;
        OrderRequestPayload payload = new OrderRequestPayload(customerName, bookId);

        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer 6ee232327ce7f4d7ecd3e44e1bd57b55df722479f8701bff5cb4d5722ceab093")
                .body(payload)
                .when()
                .post("/orders")
                .then()
                .statusCode(201)
                .body("created", equalTo(true))
                .body("orderId", notNullValue());

    }


    @Test
    public void getAllOrdersTest(){

        given()
                .header("Authorization", "Bearer 6ee232327ce7f4d7ecd3e44e1bd57b55df722479f8701bff5cb4d5722ceab093")
                .when()
                .get("/orders")
                .then()
                .statusCode(200)
                .body("size()", greaterThan(0));
    }

    @Test
    public void getOneOrderTest(){

        String orderId = "NWUnO75k3JN23peileFwE";
        int bookId = 3;

        Response response = given()
                .header("Authorization", "Bearer 6ee232327ce7f4d7ecd3e44e1bd57b55df722479f8701bff5cb4d5722ceab093")
                .pathParams("orderId", orderId)
                .when()
                .get("/orders/{orderId}")
                .then()
                .statusCode(200)
                .extract()
                .response();

        OrderResponsePayload orderResponsePayload = response.as(OrderResponsePayload.class);
        Assert.assertEquals(orderId, orderResponsePayload.getId());
        Assert.assertEquals(bookId, orderResponsePayload.getBookId());
//        Assert.assertEquals("Walter Lawson", orderResponsePayload.getCustomerName());
    }


    @Test
    public void patchOrderTest() {
        String orderId = "NWUnO75k3JN23peileFwE";
        OrderUpdateRequestPayload updateRequestPayload = new OrderUpdateRequestPayload("John Test");

        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer 6ee232327ce7f4d7ecd3e44e1bd57b55df722479f8701bff5cb4d5722ceab093")
                .body(updateRequestPayload)
                .pathParam("orderId", orderId)
                .when()
                .patch("/orders/{orderId}")
                .then()
                .statusCode(204);
    }

    @Test
    public void deleteOrderTest() throws InterruptedException {

        String customerName = faker.name().fullName();
        int bookId = 3;
        OrderRequestPayload payload = new OrderRequestPayload(customerName, bookId);

        Response response = given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer 6ee232327ce7f4d7ecd3e44e1bd57b55df722479f8701bff5cb4d5722ceab093")
                .body(payload)
                .when()
                .post("/orders")
                .then()
                .statusCode(201)
                .extract()
                .response();

        String orderId = response.jsonPath().get("orderId");

        given()
                .header("Authorization", "Bearer 6ee232327ce7f4d7ecd3e44e1bd57b55df722479f8701bff5cb4d5722ceab093")
                .pathParam("orderId", orderId)
                .when()
                .delete("/orders/{orderId}")
                .then()
                .statusCode(204);
    }


}
