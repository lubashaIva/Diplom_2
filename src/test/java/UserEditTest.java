import com.github.javafaker.Faker;
import org.junit.Before;
import org.resources.Resources;
import org.steps.UserSteps;
import io.qameta.allure.junit4.DisplayName;
import jdk.jfr.Description;
import org.junit.After;
import org.junit.Test;

import org.pojo.UserCreateRequest;
import org.pojo.UserLoginRequest;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.equalTo;

public class UserEditTest {
    private final Faker faker = new Faker();
    private String email, newEmail, name, newName, password, newPassword = "";

    @After
    public  void deleteUser() {
        UserSteps userSteps = new UserSteps();
        UserLoginRequest userLoginRequest = new UserLoginRequest(email, password);
        userSteps.userDeleteAfterLogin(userLoginRequest);
        clean();
    }

    private void clean() {
        email = "";
        newEmail = "";
        name = "";
        newName = "";
        password = "";
        newPassword = "";
    }

    @Before
    public void setupFaker() {
        email = faker.internet().emailAddress();
        newEmail = faker.internet().emailAddress();
        password = faker.internet().password();
        newPassword = faker.internet().password();
        name = faker.name().firstName();
        newName = faker.name().firstName();
    }

    @Test
    @DisplayName("Обновление email с авторизацией")
    @Description("Проверка возможности обновления поля email с авторизацией")
    public void userEditEmailWithAuthorization() {

        UserCreateRequest userCreateRequest = new UserCreateRequest(email, password, name);
        UserCreateRequest userEditRequest = new UserCreateRequest(newEmail, password, name);
        UserLoginRequest userLoginRequest = new UserLoginRequest(email, password);
        UserSteps userSteps = new UserSteps();

        userSteps.userCreate(userCreateRequest);

        userSteps.userEditAfterLogin(userLoginRequest, userEditRequest)
                .statusCode(SC_OK)
                .and()
                .assertThat().body("success", equalTo(true))
                .and()
                .assertThat().body("user.email", equalTo(newEmail));

        email = newEmail;
    }

    @Test
    @DisplayName("Обновление email без авторизации")
    @Description("Проверка не возможности обновления поля email без авторизации")
    public void userEditEmailWithoutAuthorization() {

        UserCreateRequest userCreateAndEditRequest = new UserCreateRequest(email, password, name);
        UserCreateRequest userEditRequest = new UserCreateRequest(newEmail, password, name);
        UserSteps userSteps = new UserSteps();

        userSteps.userCreate(userCreateAndEditRequest);

        userSteps.userEdit(userEditRequest)
                .statusCode(SC_UNAUTHORIZED)
                .and()
                .assertThat().body("message", equalTo(Resources.YOU_SHOULD_BE_AUTHORISED));

    }

    @Test
    @DisplayName("Обновление password с авторизацией")
    @Description("Проверка возможности обновления поля password с авторизацией")
    public void userEditPasswordWithAuthorization() {

        UserCreateRequest userCreateRequest = new UserCreateRequest(email, password, name);
        UserCreateRequest userEditRequest = new UserCreateRequest(email, newPassword, name);
        UserLoginRequest userLoginRequest = new UserLoginRequest(email, password);
        UserLoginRequest userNewLoginRequest = new UserLoginRequest(email, newPassword);
        UserSteps userSteps = new UserSteps();

        userSteps.userCreate(userCreateRequest);

        userSteps.userEditAfterLogin(userLoginRequest, userEditRequest)
                .statusCode(SC_OK)
                .and()
                .assertThat().body("success", equalTo(true));

        userSteps.userLogin(userNewLoginRequest)
                .statusCode(SC_OK)
                .and()
                .assertThat().body("success", equalTo(true));
        password = newPassword;
    }

    @Test
    @DisplayName("Обновление password без авторизации")
    @Description("Проверка не возможности обновления поля password без авторизации")
    public void userEditPasswordWithoutAuthorization() {

        UserCreateRequest userCreateAndEditRequest = new UserCreateRequest(email, password, name);
        UserCreateRequest userEditRequest = new UserCreateRequest(email, newPassword, name);
        UserSteps userSteps = new UserSteps();

        userSteps.userCreate(userCreateAndEditRequest);

        userSteps.userEdit(userEditRequest)
                .statusCode(SC_UNAUTHORIZED)
                .and()
                .assertThat().body("message", equalTo(Resources.YOU_SHOULD_BE_AUTHORISED));

    }

    @Test
    @DisplayName("Обновление name с авторизацией")
    @Description("Проверка возможности обновления поля name с авторизацией")
    public void userEditNameWithAuthorization() {

        UserCreateRequest userCreateAndEditRequest = new UserCreateRequest(email, password, name);
        UserCreateRequest userEditRequest = new UserCreateRequest(email, password, newName);
        UserLoginRequest userLoginRequest = new UserLoginRequest(email, password);
        UserSteps userSteps = new UserSteps();

        userSteps.userCreate(userCreateAndEditRequest);

        userSteps.userEditAfterLogin(userLoginRequest, userEditRequest)
                .statusCode(SC_OK)
                .and()
                .assertThat().body("success", equalTo(true))
                .and()
                .assertThat().body("user.name", equalTo(newName));

    }

    @Test
    @DisplayName("Обновление name без авторизации")
    @Description("Проверка не возможности обновления поля name без авторизации")
    public void userEditNameWithoutAuthorization() {

        UserCreateRequest userCreateRequest = new UserCreateRequest(email, password, name);
        UserCreateRequest userEditRequest = new UserCreateRequest(email, password, newName);
        UserSteps userSteps = new UserSteps();

        userSteps.userCreate(userCreateRequest);

        userSteps.userEdit(userEditRequest)
                .statusCode(SC_UNAUTHORIZED)
                .and()
                .assertThat().body("message", equalTo(Resources.YOU_SHOULD_BE_AUTHORISED));

    }

}
