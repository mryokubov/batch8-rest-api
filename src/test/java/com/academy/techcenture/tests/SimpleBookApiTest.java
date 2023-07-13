package com.academy.techcenture.tests;

import com.academy.techcenture.pojos.*;
import com.github.javafaker.Faker;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import static com.academy.techcenture.api.ApiOperations.*;
import static com.academy.techcenture.end_points.ApiEndPoints.*;
import static org.hamcrest.Matchers.*;

public class SimpleBookApiTest {

    private Faker faker = new Faker();


    @BeforeClass
    public static void setUp(){
        RestAssured.baseURI = BASE_URL;
    }


    @Test
    public void getApiStatusTest(){
        performGetRequest(GET_STATUS_ENDPOINT, false)
                        .then()
                        .statusCode(200)
                        .body("status", equalTo("OK"));
    }


    @Test
    public void getAllBooksTest(){
        performGetRequest(GET_ALL_BOOKS_ENDPOINT, false)
                .then()
                .statusCode(200)
                .body("size()", equalTo(6));
    }

    @Test
    public void getAllBooksVerifyEachBookTest(){

        Response response =  performGetRequest(GET_ALL_BOOKS_ENDPOINT, false)
                .then()
                .statusCode(200)
                .extract()
                .response();

        BookLimitedDetailsResponse[] booksResponse = response.as(BookLimitedDetailsResponse[].class);
        for (BookLimitedDetailsResponse bookLimitedDetailsResponse : booksResponse) {
           Assert.assertTrue(bookLimitedDetailsResponse.getId() != null);
        }

    }

    @Test
    public void getAllFictionBooksTest(){
        performGetRequestQueryParam(GET_ALL_BOOKS_ENDPOINT, "type", "fiction", false)
                .then()
                .statusCode(200)
                .body("size()", equalTo(4));

    }

    @Test
    public void getAllNonFictionBooksTest(){
        performGetRequestQueryParam(GET_ALL_BOOKS_ENDPOINT, "type", "non-fiction", false)
                .then()
                .statusCode(200)
                .body("size()", equalTo(2));
    }

    @Test
    public void getAllBooksWithLimitTest(){
        performGetRequestQueryParam(GET_ALL_BOOKS_ENDPOINT, "limit", "2", false)
                .then()
                .statusCode(200)
                .body("size()", equalTo(2));
    }

    @Test
    public void getOneRandomBookTest(){
        int bookId = (int)(Math.random() * 6) + 1;
        performGetRequestPathParam(GET_ONE_BOOK_ENDPOINT, "bookId", String.valueOf(bookId), false)
                    .then()
                    .statusCode(200)
                    .body("id", equalTo(bookId));
    }

    @Test
    public void getOneBookTest(){

        int bookId = 1;
        Response response = performGetRequestPathParam(GET_ONE_BOOK_ENDPOINT, "bookId", String.valueOf(bookId), false)
                .then()
                .statusCode(200)
                .extract()
                .response();

        BookFullDetailsResponse bookResponse = response.as(BookFullDetailsResponse.class);
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
        String strPayload = "{\n" +
                "  \"bookId\": "+bookId+",\n" +
                "  \"customerName\": \""+customerName+"\"" +
                "}";

        performPostRequest(POST_ORDERS_ENDPOINT, strPayload)
                    .then()
                    .statusCode(201)
                    .body("created", equalTo(true))
                    .body("orderId", notNullValue());

    }

    @Test
    public void submitBookOrderTest(){

        String customerName = faker.name().fullName();
        int bookId = 3;
        OrderBookRequest payload = new OrderBookRequest(customerName, bookId);

        performPostRequest(POST_ORDERS_ENDPOINT, payload)
                .then()
                .statusCode(201)
                .body("created", equalTo(true))
                .body("orderId", notNullValue());
    }


    @Test
    public void getAllOrdersTest(){
        performGetRequest(GET_All_ORDERS_ENDPOINT, true)
                .then()
                .statusCode(200)
                .body("size()", greaterThan(0));
    }

    @Test
    public void getOneOrderTest(){
        String orderId = "ZqFFjJ1jdeZtA-6mN7NLR";
        int bookId = 1;
        Response response = performGetRequestPathParam(GET_ONE_ORDER_ENDPOINT, "orderId", orderId, true)
                .then()
                .statusCode(200)
                .extract()
                .response();

        OrderResponse orderResponsePayload = response.as(OrderResponse.class);
        Assert.assertEquals(orderId, orderResponsePayload.getId());
        Assert.assertEquals(bookId, orderResponsePayload.getBookId());
    }


    @Test
    public void patchOrderTest() {

        String customerName = faker.name().fullName();
        int bookId = 3;
        OrderBookRequest payload = new OrderBookRequest(customerName, bookId);

        Response response = performPostRequest(POST_ORDERS_ENDPOINT, payload)
                .then()
                .statusCode(201)
                .extract()
                .response();

        String orderId = response.jsonPath().get("orderId");
        OrderUpdateRequestPayload updateRequestPayload = new OrderUpdateRequestPayload("John Test");

        performPatchRequest(PATCH_ONE_ORDER_ENDPOINT, "orderId", orderId, updateRequestPayload)
                .then()
                .statusCode(204);
    }

    @Test
    public void deleteOrderTest() {

        String customerName = faker.name().fullName();
        int bookId = 1;
        OrderBookRequest payload = new OrderBookRequest(customerName, bookId);

        Response response = performPostRequest(POST_ORDERS_ENDPOINT, payload)
                .then()
                .statusCode(201)
                .extract()
                .response();

        String orderId = response.jsonPath().get("orderId");
        System.out.println(orderId);

        performDeleteRequest(DELETE_ONE_ORDER_ENDPOINT, "orderId", orderId)
                .then()
                .statusCode(204);
    }

}
