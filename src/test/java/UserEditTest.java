import org.Resources.Resources;
import org.steps.UserSteps;
import io.qameta.allure.junit4.DisplayName;
import jdk.jfr.Description;
import org.junit.After;
import org.junit.Test;

import org.pojo.UserCreateRequest;
import org.pojo.UserLoginRequest;

import static org.hamcrest.CoreMatchers.equalTo;

public class UserEditTest {

    @After
    public  void deleteUser() {
        UserSteps userSteps = new UserSteps();
        UserLoginRequest userLoginRequest = new UserLoginRequest(Resources.email, Resources.password);
        userSteps.userDeleteAfterLogin(userLoginRequest);
    }

    @Test
    @DisplayName("Обновление email с авторизацией")
    @Description("Проверка возможности обновления поля email с авторизацией")
    public void userEditEmailWithAuthorization() {

        UserCreateRequest userCreateRequest = new UserCreateRequest(Resources.email, Resources.password, Resources.name);
        UserCreateRequest userEditRequest = new UserCreateRequest(Resources.newEmail, Resources.password, Resources.name);
        UserLoginRequest userLoginRequest = new UserLoginRequest(Resources.email, Resources.password);
        UserLoginRequest newUserLoginRequest = new UserLoginRequest(Resources.newEmail, Resources.password);
        UserSteps userSteps = new UserSteps();

        userSteps.userCreate(userCreateRequest);

        userSteps.userEditAfterLogin(userLoginRequest, userEditRequest)
                .assertThat().body("success", equalTo(true))
                .and()
                .assertThat().body("user.email", equalTo(Resources.newEmail))
                .and()
                .statusCode(200);

        userSteps.userDeleteAfterLogin(newUserLoginRequest);
    }

    @Test
    @DisplayName("Обновление email без авторизации")
    @Description("Проверка не возможности обновления поля email без авторизации")
    public void userEditEmailWithoutAuthorization() {

        UserCreateRequest userCreateAndEditRequest = new UserCreateRequest(Resources.email, Resources.password, Resources.name);
        UserCreateRequest userEditRequest = new UserCreateRequest(Resources.newEmail, Resources.password, Resources.name);
        UserSteps userSteps = new UserSteps();

        userSteps.userCreate(userCreateAndEditRequest);

        userSteps.userEdit(userEditRequest)
                .assertThat().body("success", equalTo(false))
                .and()
                .statusCode(401);

    }

    @Test
    @DisplayName("Обновление password с авторизацией")
    @Description("Проверка возможности обновления поля password с авторизацией")
    public void userEditPasswordWithAuthorization() {

        UserCreateRequest userCreateRequest = new UserCreateRequest(Resources.email, Resources.password, Resources.name);
        UserCreateRequest userEditRequest = new UserCreateRequest(Resources.email, Resources.newPassword, Resources.name);
        UserLoginRequest userLoginRequest = new UserLoginRequest(Resources.email, Resources.password);
        UserLoginRequest userNewLoginRequest = new UserLoginRequest(Resources.email, Resources.newPassword);
        UserSteps userSteps = new UserSteps();

        userSteps.userCreate(userCreateRequest);

        userSteps.userEditAfterLogin(userLoginRequest, userEditRequest)
                .assertThat().body("success", equalTo(true))
                .and()
                .statusCode(200);

        userSteps.userLogin(userNewLoginRequest)
                .assertThat().body("success", equalTo(true))
                .and()
                .statusCode(200);

    }

    @Test
    @DisplayName("Обновление password без авторизации")
    @Description("Проверка не возможности обновления поля password без авторизации")
    public void userEditPasswordWithoutAuthorization() {

        UserCreateRequest userCreateAndEditRequest = new UserCreateRequest(Resources.email, Resources.password, Resources.name);
        UserCreateRequest userEditRequest = new UserCreateRequest(Resources.email, Resources.newPassword, Resources.name);
        UserSteps userSteps = new UserSteps();

        userSteps.userCreate(userCreateAndEditRequest);

        userSteps.userEdit(userEditRequest)
                .assertThat().body("success", equalTo(false))
                .and()
                .statusCode(401);

    }

    @Test
    @DisplayName("Обновление name с авторизацией")
    @Description("Проверка возможности обновления поля name с авторизацией")
    public void userEditNameWithAuthorization() {

        UserCreateRequest userCreateAndEditRequest = new UserCreateRequest(Resources.email, Resources.password, Resources.name);
        UserCreateRequest userEditRequest = new UserCreateRequest(Resources.email, Resources.password, Resources.newName);
        UserLoginRequest userLoginRequest = new UserLoginRequest(Resources.email, Resources.password);
        UserSteps userSteps = new UserSteps();

        userSteps.userCreate(userCreateAndEditRequest);

        userSteps.userEditAfterLogin(userLoginRequest, userEditRequest)
                .assertThat().body("success", equalTo(true))
                .and()
                .assertThat().body("user.name", equalTo(Resources.newName))
                .and()
                .statusCode(200);

    }

    @Test
    @DisplayName("Обновление name без авторизации")
    @Description("Проверка не возможности обновления поля name без авторизации")
    public void userEditNameWithoutAuthorization() {

        UserCreateRequest userCreateRequest = new UserCreateRequest(Resources.email, Resources.password, Resources.name);
        UserCreateRequest userEditRequest = new UserCreateRequest(Resources.email, Resources.password, Resources.newName);
        UserSteps userSteps = new UserSteps();

        userSteps.userCreate(userCreateRequest);

        userSteps.userEdit(userEditRequest)
                .assertThat().body("success", equalTo(false))
                .and()
                .statusCode(401);

    }

}
