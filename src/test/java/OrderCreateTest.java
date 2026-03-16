
import com.github.javafaker.Faker;
import org.junit.Before;
import org.resources.Resources;
import org.steps.OrderSteps;
import org.steps.UserSteps;
import io.qameta.allure.junit4.DisplayName;
import jdk.jfr.Description;
import org.junit.After;
import org.junit.Test;
import org.pojo.OrderCreateRequest;
import org.pojo.UserCreateRequest;
import org.pojo.UserLoginRequest;

import java.util.ArrayList;
import java.util.List;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.isA;


public class OrderCreateTest {
    
    public static List<String> ingredients = new ArrayList<>();
    private final Faker faker = new Faker();
    private String email = "";
    private String name = "";
    private String password = "";

    @After
    public  void deleteUser() {
        UserSteps userSteps = new UserSteps();
        UserLoginRequest userLoginRequest = new UserLoginRequest(email, password);
        userSteps.userDeleteAfterLogin(userLoginRequest);
        clean();
    }

    private void clean() {
        ingredients.clear();
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
    @DisplayName("Создание заказа после авторизации")
    @Description("Проверка возможности создания заказа после авторизации")
    public void orderCreateWithAuthorization() {

        UserCreateRequest userCreateRequest = new UserCreateRequest(email, password, name);
        UserLoginRequest userLoginRequest = new UserLoginRequest(email, password);
        ingredients.add("61c0c5a71d1f82001bdaaa6d");
        OrderCreateRequest orderCreateRequest = new OrderCreateRequest(ingredients);
        UserSteps userSteps = new UserSteps();
        OrderSteps orderSteps = new OrderSteps();

        userSteps.userCreate(userCreateRequest);
        orderSteps.orderCreateAfterLogin(userLoginRequest, orderCreateRequest)
                .statusCode(SC_OK)
                .and()
                .assertThat().body("success", equalTo(true))
                .and()
                .assertThat().body("order.owner.email", equalTo(email));
    }

    @Test
    @DisplayName("Создание заказа без авторизации")
    @Description("Проверка возможности создания заказа без авторизации")
    public void orderCreateWithoutAuthorization() {

        UserCreateRequest userCreateRequest = new UserCreateRequest(email, password, name);
        ingredients.add("61c0c5a71d1f82001bdaaa6d");
        OrderCreateRequest orderCreateRequest = new OrderCreateRequest(ingredients);
        UserSteps userSteps = new UserSteps();
        OrderSteps orderSteps = new OrderSteps();

        userSteps.userCreate(userCreateRequest);
        orderSteps.orderCreate(orderCreateRequest)
                .statusCode(SC_OK)
                .and()
                .assertThat().body("success", equalTo(true))
                .and()
                .assertThat().body("order.number", isA(Integer.class));

    }

    @Test
    @DisplayName("Создание заказа после авторизации без ингредиентов")
    @Description("Проверка не возможности создания заказа после авторизации без ингредиентов")
    public void orderCreateWithAuthorizationWithoutIngredients() {

        UserCreateRequest userCreateRequest = new UserCreateRequest(email, password, name);
        UserLoginRequest userLoginRequest = new UserLoginRequest(email, password);
        OrderCreateRequest orderCreateRequest = new OrderCreateRequest(ingredients);
        UserSteps userSteps = new UserSteps();
        OrderSteps orderSteps = new OrderSteps();

        userSteps.userCreate(userCreateRequest);
        orderSteps.orderCreateAfterLogin(userLoginRequest, orderCreateRequest)
                .statusCode(SC_BAD_REQUEST)
                .and()
                .assertThat().body("message", equalTo(Resources.NO_INGREDIENTS_ERROR));

    }

    @Test
    @DisplayName("Создание заказа после авторизации с неверным ингредиентом")
    @Description("Проверка не возможности создания заказа после авторизации без ингредиентов")
    public void orderCreateWithAuthorizationWithWrongIngredients() {

        UserCreateRequest userCreateRequest = new UserCreateRequest(email, password, name);
        UserLoginRequest userLoginRequest = new UserLoginRequest(email, password);
        ingredients.add("61c0c5a71d1f82001bdaaa6d");
        ingredients.add("wrongIngredients");
        OrderCreateRequest orderCreateRequest = new OrderCreateRequest(ingredients);
        UserSteps userSteps = new UserSteps();
        OrderSteps orderSteps = new OrderSteps();

        userSteps.userCreate(userCreateRequest);
        orderSteps.orderCreateAfterLogin(userLoginRequest, orderCreateRequest)
                .assertThat().statusCode(SC_INTERNAL_SERVER_ERROR);

    }

}