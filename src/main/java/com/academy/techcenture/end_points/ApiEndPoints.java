package com.academy.techcenture.end_points;

public class ApiEndPoints {

    public final static String BASE_URL = "https://simple-books-api.glitch.me";
    public final static String GET_STATUS_ENDPOINT = "/status";
    public final static String GET_ALL_BOOKS_ENDPOINT = "/books";
    public final static String GET_ONE_BOOK_ENDPOINT =  GET_ALL_BOOKS_ENDPOINT + "/{bookId}";
    public final static String POST_ORDERS_ENDPOINT = "/orders";
    public final static String GET_All_ORDERS_ENDPOINT = "/orders";
    public final static String GET_ONE_ORDER_ENDPOINT = GET_All_ORDERS_ENDPOINT + "/{orderId}";
    public final static String PATCH_ONE_ORDER_ENDPOINT = GET_All_ORDERS_ENDPOINT + "/{orderId}";
    public final static String DELETE_ONE_ORDER_ENDPOINT = "/orders/{orderId}";
    public final static String POST_REGISTER_CLIENT_ENDPOINT = "/api-clients";

}
