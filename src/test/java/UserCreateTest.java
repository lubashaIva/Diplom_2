import org.Resources.Resources;
import org.steps.UserSteps;

import io.qameta.allure.junit4.DisplayName;
import jdk.jfr.Description;
import org.junit.After;
import org.junit.Test;
import org.pojo.UserCreateRequest;
import org.pojo.UserLoginRequest;

import static org.hamcrest.CoreMatchers.equalTo;

public class UserCreateTest {

    private boolean skipDeleteUser = false;

    @After
    public void deleteUser() {
        if (!skipDeleteUser) {
            UserSteps userSteps = new UserSteps();
            UserLoginRequest userLoginRequest = new UserLoginRequest(Resources.email, Resources.password);
            userSteps.userDeleteAfterLogin(userLoginRequest);
        }
    }

    @Test
    @DisplayName("Создание уникального пользователя")
    @Description("Проверка возможности создать нового уникального пользователя")
    public void createNewUser() {
        skipDeleteUser = false;

        UserCreateRequest userCreateAndEditRequest = new UserCreateRequest(Resources.email, Resources.password, Resources.name);
        UserSteps userSteps = new UserSteps();

        userSteps.userCreate(userCreateAndEditRequest)
                .assertThat().body("success", equalTo(true))
                .and()
                .statusCode(200);

    }

    @Test
    @DisplayName("Создание пользователя, который уже зарегистрирован")
    @Description("Проверка не возможности создать пользователя, который уже зарегистрирован")
    public void createDuplicateUser() {
        skipDeleteUser = false;

        UserCreateRequest userCreateAndEditRequest = new UserCreateRequest(Resources.email, Resources.password, Resources.name);
        UserSteps userSteps = new UserSteps();

        userSteps.userCreate(userCreateAndEditRequest);

        userSteps.userCreate(userCreateAndEditRequest)
                .assertThat().body("success", equalTo(false))
                .and()
                .statusCode(403);

    }

    @Test
    @DisplayName("Создание пользователя без поля email")
    @Description("Проверка не возможности создать пользователя без поля email")
    public void createUserWithoutEmail() {

        skipDeleteUser = true;

        UserCreateRequest userCreateAndEditRequest = new UserCreateRequest(null, Resources.password, Resources.name);
        UserSteps userSteps = new UserSteps();

        userSteps.userCreate(userCreateAndEditRequest)
                .assertThat().body("success", equalTo(false))
                .and()
                .statusCode(403);
    }

    @Test
    @DisplayName("Создание пользователя без поля password")
    @Description("Проверка не возможности создать пользователя без поля password")
    public void createUserWithoutPassword() {

        skipDeleteUser = true;

        UserCreateRequest userCreateAndEditRequest = new UserCreateRequest(Resources.email, null, Resources.name);
        UserSteps userSteps = new UserSteps();

        userSteps.userCreate(userCreateAndEditRequest)
                .assertThat().body("success", equalTo(false))
                .and()
                .statusCode(403);
    }

    @Test
    @DisplayName("Создание пользователя без поля name")
    @Description("Проверка не возможности создать пользователя без поля name")
    public void createUserWithoutName() {

        skipDeleteUser = true;

        UserCreateRequest userCreateAndEditRequest = new UserCreateRequest(Resources.email, Resources.password, null);
        UserSteps userSteps = new UserSteps();

        userSteps.userCreate(userCreateAndEditRequest)
                .assertThat().body("success", equalTo(false))
                .and()
                .statusCode(403);
    }

}
