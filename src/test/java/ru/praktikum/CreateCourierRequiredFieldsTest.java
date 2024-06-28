package ru.praktikum;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import org.apache.commons.lang3.RandomStringUtils;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ru.praktikum.model.Courier;
import ru.praktikum.steps.CourierSteps;

import static ru.praktikum.steps.CourierSteps.BASE_URL;


@RunWith(Parameterized.class)
public class CreateCourierRequiredFieldsTest {

    private String login;
    private String password;
    private String firstName;
    private Courier courier;
    private CourierSteps courierSteps = new CourierSteps();


    public CreateCourierRequiredFieldsTest(String login, String password, String firstName) {
        this.login = login;
        this.password = password;
        this.firstName = firstName;
    }

    @Parameterized.Parameters
    public static Object[][] getTestData() {
        // Тестовые данные
        return new Object[][] {
                {null, null, null},
                {null, null, RandomStringUtils.randomAlphabetic(10)},
                {null, RandomStringUtils.randomAlphabetic(10), null},
                {RandomStringUtils.randomAlphabetic(10), null, null}
        };
    }

    @Before
    public void setUp() {
        RestAssured.requestSpecification = new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .setBaseUri(BASE_URL)
                .build();
    }

    @Test
    @DisplayName("[400] [POST /api/v1/courier] Create courier. Request without required fields.")
    public void shouldReturnErrorIfFieldIsEmpty() {

        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());

        courier = new Courier();
        courier.setLogin(login);
        courier.setPassword(password);
        courier.setFirstName(firstName);

        courierSteps
                .createCourier(courier)
                .statusCode(400)
                .body("code", Matchers.is(400))
                .body("message", Matchers.is("Недостаточно данных для создания учетной записи"));
    }
}
