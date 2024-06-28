package ru.praktikum;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.praktikum.model.Courier;
import ru.praktikum.steps.CourierSteps;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

public class LoginCourierTest {

    private static final String BASE_URI="https://qa-scooter.praktikum-services.ru";
    private static final String CREATE_ENDPOINT="/api/v1/courier";
    private static final String LOGIN_ENDPOINT="/api/v1/courier/login";
    Courier courier;
    private CourierSteps courierSteps = new CourierSteps();
    //private String login;
    //private String password;

    @Before
    public void setUp(){
        //RestAssured.baseURI=BASE_URI;
        RestAssured.filters(new RequestLoggingFilter());

        courier = new Courier();
        courier.setLogin(randomAlphabetic(10));
        courier.setPassword(randomAlphabetic(10));

    }

    @Test
    @DisplayName("[200] [POST /api/v1/courier/login] Correct request to login courier should return id")
    public void shouldReturnId() {

        courierSteps
                .createCourier(courier);

        courierSteps
                .login(courier)
                .statusCode(200)
                .body("id", notNullValue());

    }

    @Test
    @DisplayName("[404] [POST /api/v1/courier/login] Account doesn't exist")
    public void shouldReturnErrorLoginCourier() {

        courierSteps
                .createCourier(courier);

        String originalLogin = courier.getLogin();
        String originalPassword = courier.getPassword();

        courier.setLogin(randomAlphabetic(10));
        courier.setPassword(randomAlphabetic(10));

        courierSteps
                .login(courier)
                .statusCode(404)
                .body("code", is(404))
                .body("message", is("Учетная запись не найдена"));

        courier.setLogin(originalLogin);
        courier.setPassword(originalPassword);
    }

    @After
    public void tearDown(){

        Integer id = courierSteps.login(courier)
                .extract().body().path("id");
        courier.setId(id);
        courierSteps.delete(courier);

    }
}
