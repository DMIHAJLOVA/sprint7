package ru.praktikum.steps;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import ru.praktikum.model.Order;

import static io.restassured.RestAssured.given;

public class OrderSteps {
    private static final String CREATE_ORDER = "/api/v1/orders";
    private static final String CANCEL_ORDER = "/api/v1/orders/cancel";
    private static final String GET_ALL_ORDERS = "/api/v1/orders";
    private static final String GET_ORDER_BY_ID = "/api/v1/orders/track";
    private static final String BASE_URL="https://qa-scooter.praktikum-services.ru";

    @Step("POST /api/v1/orders/cancel create order")
    public Response createOrder(Order order) {
        return given()
                .baseUri(BASE_URL)
                .body(order)
                .when()
                .post(CREATE_ORDER);
    }

    @Step("PUT /api/v1/orders/cancel cansel order")
    public Response cancelOrder(Order order) {
        return given()
                .baseUri(BASE_URL)
                .queryParam("track", order.getTrackId())
                .when()
                .put(CANCEL_ORDER);
    }

    @Step("GET /api/v1/orders get lists orders")
    public Response getAllOrders(Integer courierId, String nearestStation, Integer limit, Integer page) {

        RequestSpecification request = given()
                .baseUri(BASE_URL);

        if (courierId != null) {
            request.queryParam("courierId", courierId);
        }
        if (nearestStation != null) {
            request.queryParam("nearestStation", nearestStation);
        }
        if (limit != null) {
            request.queryParam("limit", limit);
        }
        if (page != null) {
            request.queryParam("page", page);
        }

        return request
                .when()
                .get(GET_ALL_ORDERS);

    }

    @Step("GET /api/v1/orders/track get order track")
    public Response getOrderByTrackId(Integer trackId) {

        RequestSpecification request = given()
                .baseUri(BASE_URL);

        if (trackId != null) {
            request.queryParam("t", trackId);
        }

        return request
                .when()
                .get(GET_ORDER_BY_ID);
    }

}
