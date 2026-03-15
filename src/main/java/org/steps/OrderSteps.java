package org.steps;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import org.pojo.OrderCreateRequest;
import org.pojo.UserLoginRequest;
import org.pojo.UserLoginResponse;

import static org.steps.UserSteps.requestSpecification;
import static org.endpoints.Endpoint.ORDERS;

public class OrderSteps {

    @Step("Создание нового заказа без авторизации")
    public ValidatableResponse orderCreate(OrderCreateRequest orderCreateRequest) {
        return requestSpecification()
                .body(orderCreateRequest)
                .post(ORDERS)
                .then();
    }

    @Step("Создание нового заказа после авторизации")
    public ValidatableResponse orderCreateAfterLogin(UserLoginRequest userLoginRequest, OrderCreateRequest orderCreateRequest) {
        UserSteps userSteps = new UserSteps();
        Response response = userSteps.userLogin(userLoginRequest)
                .extract().response();
        UserLoginResponse userLoginResponse = response.as(UserLoginResponse.class);
        String accessToken = userLoginResponse.getAccessToken();
        return requestSpecification()
                .header("Authorization", accessToken)
                .body(orderCreateRequest)
                .post(ORDERS)
                .then();
    }

    @Step("Получение заказов без авторизации")
    public ValidatableResponse orderList() {
        return requestSpecification()
                .get(ORDERS)
                .then();
    }

    @Step("Получение заказов после авторизации")
    public ValidatableResponse orderListAfterLogin(UserLoginRequest userLoginRequest) {
        UserSteps userSteps = new UserSteps();
        Response response = userSteps.userLogin(userLoginRequest)
                .extract().response();
        UserLoginResponse userLoginResponse = response.as(UserLoginResponse.class);
        String accessToken = userLoginResponse.getAccessToken();
        return requestSpecification()
                .header("Authorization", accessToken)
                .get(ORDERS)
                .then();
    }

}
