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

public class UserLoginTest {
    private final Faker faker = new Faker();
    private String email, wrongEmail, name, password, wrongPassword = "";

    @After
    public  void deleteUser() {
        UserSteps userSteps = new UserSteps();
        UserLoginRequest userLoginRequest = new UserLoginRequest(email, password);
        userSteps.userDeleteAfterLogin(userLoginRequest);
        clean();
    }

    private void clean() {
        email = "";
        wrongEmail = "";
        name = "";
        password = "";
        wrongPassword = "";
    }

    @Before
    public void setupFaker() {
        email = faker.internet().emailAddress();
        wrongEmail = faker.internet().emailAddress();
        password = faker.internet().password();
        wrongPassword = faker.internet().password();
        name = faker.name().firstName();
        createUser();
    }

    private void createUser() {
        UserCreateRequest userCreateRequest = new UserCreateRequest(email, password, name);
        UserSteps userSteps = new UserSteps();

        userSteps.userCreate(userCreateRequest);
    }

    @Test
    @DisplayName("Логин под существующим пользователем")
    @Description("Проверка возможности логина под существующим пользователем")
    public void userLogin() {

        UserLoginRequest userLoginRequest = new UserLoginRequest(email, password);
        UserSteps userSteps = new UserSteps();

        userSteps.userLogin(userLoginRequest)
                .statusCode(SC_OK)
                .and()
                .assertThat().body("success", equalTo(true));

    }

    @Test
    @DisplayName("Логин с неверным email")
    @Description("Проверка не возможности логина с неверным email")
    public void userLoginWithWrongEmail() {

        UserLoginRequest userWrongLoginRequest = new UserLoginRequest(wrongEmail, password);
        UserSteps userSteps = new UserSteps();

        userSteps.userLogin(userWrongLoginRequest)
                .statusCode(SC_UNAUTHORIZED)
                .and()
                .assertThat().body("message", equalTo(Resources.EMAIL_OR_PASSWORD_ARE_INCORRECT));

    }

    @Test
    @DisplayName("Вход с неверным паролем")
    @Description("Проверка не возможности входа с неверным поралем")
    public void userLoginWithWrongPassword() {

        UserLoginRequest userWrongLoginRequest = new UserLoginRequest(email, wrongPassword);
        UserSteps userSteps = new UserSteps();

        userSteps.userLogin(userWrongLoginRequest)
                .statusCode(SC_UNAUTHORIZED)
                .and()
                .assertThat().body("message", equalTo(Resources.EMAIL_OR_PASSWORD_ARE_INCORRECT));

    }

}