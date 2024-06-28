package ru.praktikum;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ru.praktikum.model.Courier;
import ru.praktikum.steps.CourierSteps;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.hamcrest.CoreMatchers.is;


@RunWith(Parameterized.class)
public class LoginCourierRequiredFieldsTest {

    private String login;
    private String password;
    private Courier courier;

    public LoginCourierRequiredFieldsTest(String login, String password) {
        this.login = login;
        this.password = password;
    }

    @Parameterized.Parameters
    public static Object[][] getTestData() {
        // Тестовые данные
        return new Object[][] {
                //{null, null},
                {null, randomAlphabetic(5)}//,
                //{randomAlphabetic(5), null}
        };
    }

    @Test
    @DisplayName("[400] [POST /api/v1/courier/login] Login courier. Request without required fields.")
    public void shouldReturnErrorToLoginCourier() {

        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());

        courier = new Courier();
        courier.setLogin(login);
        courier.setPassword(password);

        new CourierSteps()
                .login(courier)
                .statusCode(400)
                .body("code", is(400))
                .body("message", is("Недостаточно данных для входа"));
    }
}
