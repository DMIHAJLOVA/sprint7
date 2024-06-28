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

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

public class CourierTest {

    private static final String BASE_URI="https://qa-scooter.praktikum-services.ru";
    private static final String CREATE_ENDPOINT="/api/v1/courier";
    private static final String LOGIN_ENDPOINT="/api/v1/courier/login";
    Courier courier;
    private CourierSteps courierSteps = new CourierSteps();

    @Before
    public void setUp(){
           RestAssured.filters(new RequestLoggingFilter());

        courier = new Courier();
        courier.setLogin(RandomStringUtils.randomAlphabetic(10));
        courier.setPassword(RandomStringUtils.randomAlphabetic(10));

    }

    @Test
    @DisplayName("[201] [POST /api/v1/courier] Create courier. Correct request")
    public  void shouldReturnOkTrue(){
        courierSteps
                .createCourier(courier)
                .statusCode(201)
                .body("ok",is(true));
    }

    @Test
    @DisplayName("[200] [POST /api/v1/courier] Create courier. Correct request")
    public void shouldReturnId(){
        courierSteps
                .createCourier(courier);

        courierSteps
                .login(courier)
                .statusCode(200)
                .body("id",notNullValue());

    }
     @Test
     @DisplayName("[409] [POST /api/v1/courier] Create courier. Courier is already created")
    public void shouldReturn409IfLoginAlreadyExist() {

        courierSteps
                .createCourier(courier);

        courierSteps
                .createCourier(courier)
                .statusCode(409)
                .body("code", is(409))
                .body("message", is("Этот логин уже используется. Попробуйте другой."));
    }


    @After
    public void tearDown(){

        Integer id = courierSteps.login(courier)
                .extract().body().path("id");
        courier.setId(id);
        courierSteps.delete(courier);

    }

}
