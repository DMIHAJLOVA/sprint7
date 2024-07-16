package ru.praktikum;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ru.praktikum.model.Colours;
import ru.praktikum.model.Order;
import ru.praktikum.steps.OrderSteps;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.apache.commons.lang3.RandomStringUtils.randomNumeric;

@RunWith(Parameterized.class)
public class OrderTest {

    private OrderSteps orderSteps = new OrderSteps();
    private Order order;
    private int expectedCode = 201;

    public OrderTest(String firstName, String lastName, String address, String metroStation,
                           Integer rentTime, String deliveryDate, String[] colour) {

        order = new Order();
        order.setFirstName(firstName);
        order.setLastName(lastName);
        order.setAddress(address);
        order.setMetroStation(metroStation);
        order.setRentTime(rentTime);
        order.setDeliveryDate(deliveryDate);
        order.setColor(colour);

    }

    @Parameterized.Parameters
    public static Object[][] getTestData() {

        // Тестовые данные
        return new Object[][] {
                {randomAlphabetic(10), randomAlphabetic(10), randomAlphabetic(10),
                        randomNumeric(1), Integer.valueOf(randomNumeric(3)), "2020-06-06", new String[]{ String.valueOf(Colours.BLACK)}},
                {randomAlphabetic(10), randomAlphabetic(10), randomAlphabetic(10),
                        randomNumeric(1), Integer.valueOf(randomNumeric(3)), "2024-06-06", new String[]{ String.valueOf(Colours.GREY)}},
                {randomAlphabetic(10), randomAlphabetic(10), randomAlphabetic(10),
                        randomNumeric(1), Integer.valueOf(randomNumeric(3)), "2024-06-06", new String[]{ String.valueOf(Colours.BLACK),
                        String.valueOf(Colours.GREY)}},
                {randomAlphabetic(10), randomAlphabetic(10), randomAlphabetic(10),
                        randomNumeric(1), Integer.valueOf(randomNumeric(3)), "2024-06-06", null}
        };
    }

    @Before
    public void setUp() {
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
    }

    @Test
    @DisplayName("[200] [POST /api/v1/orders] Correct request to create order")
    public void shouldReturn201AndTrack() {

        Integer trackId = orderSteps
                .createOrder(order)
                .then()
                .statusCode(expectedCode)
                .body("track", Matchers.notNullValue())
                .extract()
                .path("track");

        order.setTrackId(trackId);
    }


    @After
    public void tearDown() {
        orderSteps.cancelOrder(order);
    }

}
