package org.steps;

import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;

import org.endpoints.Endpoint;
import org.pojo.UserCreateRequest;
import org.pojo.UserLoginRequest;
import org.pojo.UserLoginResponse;


import static io.restassured.RestAssured.given;
import static org.endpoints.Endpoint.*;

public class UserSteps {

    public static RequestSpecification requestSpecification() {
        return given().log().all()
                .contentType(ContentType.JSON)
                .baseUri(Endpoint.BASE_URL);
    }

    @Step("Создание нового пользователя")
    public ValidatableResponse userCreate(UserCreateRequest userCreateRequest) {
        return requestSpecification()
                .body(userCreateRequest)
                .post(USER_CREATE)
                .then();
    }

    @Step("Авторизация пользователя")
    public ValidatableResponse userLogin(UserLoginRequest userLoginRequest) {
        return requestSpecification()
                .body(userLoginRequest)
                .post(USER_LOGIN)
                .then();
    }

    @Step("Изменение данных пользователя без авторизации")
    public ValidatableResponse userEdit(UserCreateRequest userCreateRequest) {
        return requestSpecification()
                .body(userCreateRequest)
                .patch(USER)
                .then();
    }


    @Step("Изменение данных пользователя после авторизации")
    public ValidatableResponse userEditAfterLogin(UserLoginRequest userLoginRequest, UserCreateRequest userCreateAndEditRequest) {
        Response response = userLogin(userLoginRequest)
                .extract().response();
        UserLoginResponse userLoginResponse = response.as(UserLoginResponse.class);
        String accessToken = userLoginResponse.getAccessToken();
        return requestSpecification()
                .header("Authorization", accessToken)
                .body(userCreateAndEditRequest)
                .patch(USER)
                .then();
    }

    @Step("Удаление пользователя без авторизации")
    public void userDelete(String accessToken) {
        requestSpecification()
                .header("Authorization", accessToken)
                .delete(USER)
                .then();
    }

    @Step("Удаление пользователя после авторизации")
    public void userDeleteAfterLogin(UserLoginRequest userLoginRequest) {
        Response response = userLogin(userLoginRequest)
                .extract().response();
        UserLoginResponse userLoginResponse = response.as(UserLoginResponse.class);
        String accessToken = userLoginResponse.getAccessToken();
        if (accessToken != null) {
            userDelete(accessToken);
        }
    }

}
