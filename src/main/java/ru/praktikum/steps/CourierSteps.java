package ru.praktikum.steps;

import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import ru.praktikum.model.Courier;

import static io.restassured.RestAssured.given;


public class CourierSteps {
    private static final String COURIER_CREATE = "/api/v1/courier";
    public static final String BASE_URL="https://qa-scooter.praktikum-services.ru";
    public static final String COURIER_LOGIN = "/api/v1/courier/login";
    public static final String COURIER_DELETE = "/api/v1/courier/{id}";

    @Step("POST /api/v1/courier create courier")
    public ValidatableResponse createCourier (Courier courier){
        return given()
                .contentType(ContentType.JSON)
                .baseUri(BASE_URL)
                .body(courier)
                .when()
                .post(COURIER_CREATE)
                .then();
    }

    @Step("POST /api/v1/courier/login login courier")
    public ValidatableResponse login (Courier courier){
        return given()
                .contentType(ContentType.JSON)
                .baseUri(BASE_URL)
                .body(courier)
                .when()
                .post(COURIER_LOGIN)
                .then();
    }

    @Step(" DELETE /api/v1/courier/{id} delete courier")
    public ValidatableResponse delete (Courier courier){
        return given()
                .contentType(ContentType.JSON)
                .baseUri(BASE_URL)
                .pathParams("id",courier.getId())
                .body(courier)
                .when()
                .post(COURIER_DELETE)
                .then();
    }
}
