import com.github.javafaker.Faker;
import org.junit.Before;
import org.steps.UserSteps;

import io.qameta.allure.junit4.DisplayName;
import jdk.jfr.Description;
import org.junit.After;
import org.junit.Test;
import org.pojo.UserCreateRequest;
import org.pojo.UserLoginRequest;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.equalTo;

public class UserCreateTest {

    private final Faker faker = new Faker();
    private String email, name, password = "";

    @After
    public  void deleteUser() {
        UserSteps userSteps = new UserSteps();
        UserLoginRequest userLoginRequest = new UserLoginRequest(email, password);
        userSteps.userDeleteAfterLogin(userLoginRequest);
        clean();
    }

    private void clean() {
        email = "";
        name = "";
        password = "";
    }

    @Before
    public void setupFaker() {
        email = faker.internet().emailAddress();
        password = faker.internet().password();
        name = faker.name().firstName();
    }
    
    @Test
    @DisplayName("Создание уникального пользователя")
    @Description("Проверка возможности создать нового уникального пользователя")
    public void createNewUser() {

        UserCreateRequest userCreateAndEditRequest = new UserCreateRequest(email, password, name);
        UserSteps userSteps = new UserSteps();

        userSteps.userCreate(userCreateAndEditRequest)
                .statusCode(SC_OK)
                .and()
                .assertThat().body("success", equalTo(true));

    }

    @Test
    @DisplayName("Создание пользователя, который уже зарегистрирован")
    @Description("Проверка не возможности создать пользователя, который уже зарегистрирован")
    public void createDuplicateUser() {

        UserCreateRequest userCreateAndEditRequest = new UserCreateRequest(email, password, name);
        UserSteps userSteps = new UserSteps();

        userSteps.userCreate(userCreateAndEditRequest);

        userSteps.userCreate(userCreateAndEditRequest)
                .statusCode(SC_FORBIDDEN)
                .and()
                .assertThat().body("success", equalTo(false));

    }

    @Test
    @DisplayName("Создание пользователя без поля email")
    @Description("Проверка не возможности создать пользователя без поля email")
    public void createUserWithoutEmail() {
        
        UserCreateRequest userCreateAndEditRequest = new UserCreateRequest(null, password, name);
        UserSteps userSteps = new UserSteps();

        userSteps.userCreate(userCreateAndEditRequest)
                .statusCode(SC_FORBIDDEN)
                .and()
                .assertThat().body("success", equalTo(false));
    }

    @Test
    @DisplayName("Создание пользователя без поля password")
    @Description("Проверка не возможности создать пользователя без поля password")
    public void createUserWithoutPassword() {
        
        UserCreateRequest userCreateAndEditRequest = new UserCreateRequest(email, null, name);
        UserSteps userSteps = new UserSteps();

        userSteps.userCreate(userCreateAndEditRequest)
                .statusCode(SC_FORBIDDEN)
                .and()
                .assertThat().body("success", equalTo(false));
    }

    @Test
    @DisplayName("Создание пользователя без поля name")
    @Description("Проверка не возможности создать пользователя без поля name")
    public void createUserWithoutName() {
        
        UserCreateRequest userCreateAndEditRequest = new UserCreateRequest(email, password, null);
        UserSteps userSteps = new UserSteps();

        userSteps.userCreate(userCreateAndEditRequest)
                .statusCode(SC_FORBIDDEN)
                .and()
                .assertThat().body("success", equalTo(false));
    }

}
