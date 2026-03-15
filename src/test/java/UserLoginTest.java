import org.Resources.Resources;
import org.steps.UserSteps;

import io.qameta.allure.junit4.DisplayName;
import jdk.jfr.Description;
import org.junit.After;
import org.junit.Test;
import org.pojo.UserCreateRequest;
import org.pojo.UserLoginRequest;

import static org.hamcrest.CoreMatchers.equalTo;

public class UserLoginTest {

    @After
    public  void deleteUser() {
        UserSteps userSteps = new UserSteps();
        UserLoginRequest userLoginRequest = new UserLoginRequest(Resources.email, Resources.password);
        userSteps.userDeleteAfterLogin(userLoginRequest);
    }

    @Test
    @DisplayName("Логин под существующим пользователем")
    @Description("Проверка возможности логина под существующим пользователем")
    public void userLogin() {

        UserCreateRequest userCreateRequest = new UserCreateRequest(Resources.email, Resources.password, Resources.name);
        UserLoginRequest userLoginRequest = new UserLoginRequest(Resources.email, Resources.password);
        UserSteps userSteps = new UserSteps();

        userSteps.userCreate(userCreateRequest);

        userSteps.userLogin(userLoginRequest)
                .assertThat().body("success", equalTo(true))
                .and()
                .statusCode(200);

    }

    @Test
    @DisplayName("Логин с неверным email")
    @Description("Проверка не возможности логина с неверным email")
    public void userLoginWithWrongEmail() {

        UserCreateRequest userCreateRequest = new UserCreateRequest(Resources.email, Resources.password, Resources.name);
        UserLoginRequest userWrongLoginRequest = new UserLoginRequest(Resources.wrongEmail, Resources.password);
        UserSteps userSteps = new UserSteps();

        userSteps.userCreate(userCreateRequest);

        userSteps.userLogin(userWrongLoginRequest)
                .assertThat().body("success", equalTo(false))
                .and()
                .statusCode(401);

    }

    @Test
    @DisplayName("Вход с неверным паролем")
    @Description("Проверка не возможности входа с неверным поралем")
    public void userLoginWithWrongPassword() {

        UserCreateRequest userCreateAndEditRequest = new UserCreateRequest(Resources.email, Resources.password, Resources.name);
        UserLoginRequest userWrongLoginRequest = new UserLoginRequest(Resources.email, Resources.wrongPassword);
        UserSteps userSteps = new UserSteps();

        userSteps.userCreate(userCreateAndEditRequest);

        userSteps.userLogin(userWrongLoginRequest)
                .assertThat().body("success", equalTo(false))
                .and()
                .statusCode(401);

    }

}