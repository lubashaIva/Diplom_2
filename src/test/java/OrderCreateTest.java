
import org.Resources.Resources;
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

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.isA;


public class OrderCreateTest {

    public static List<String> ingredients = new ArrayList<>();

    @After
    public  void deleteUser() {
        UserSteps userSteps = new UserSteps();
        UserLoginRequest userLoginRequest = new UserLoginRequest(Resources.email, Resources.password);
        userSteps.userDeleteAfterLogin(userLoginRequest);
    }

    @After
    public void ingredientsClean() {
        ingredients.clear();
    }

    @Test
    @DisplayName("Создание заказа после авторизации")
    @Description("Проверка возможности создания заказа после авторизации")
    public void orderCreateWithAuthorization() {

        UserCreateRequest userCreateRequest = new UserCreateRequest(Resources.email, Resources.password, Resources.name);
        UserLoginRequest userLoginRequest = new UserLoginRequest(Resources.email, Resources.password);
        ingredients.add("61c0c5a71d1f82001bdaaa6d");
        OrderCreateRequest orderCreateRequest = new OrderCreateRequest(ingredients);
        UserSteps userSteps = new UserSteps();
        OrderSteps orderSteps = new OrderSteps();

        userSteps.userCreate(userCreateRequest);
        orderSteps.orderCreateAfterLogin(userLoginRequest, orderCreateRequest)
                .assertThat().body("success", equalTo(true))
                .and()
                .assertThat().body("order.owner.email", equalTo(Resources.email))
                .and()
                .statusCode(200);

    }

    @Test
    @DisplayName("Создание заказа без авторизации")
    @Description("Проверка возможности создания заказа без авторизации")
    public void orderCreateWithoutAuthorization() {

        UserCreateRequest userCreateRequest = new UserCreateRequest(Resources.email, Resources.password, Resources.name);
        ingredients.add("61c0c5a71d1f82001bdaaa6d");
        OrderCreateRequest orderCreateRequest = new OrderCreateRequest(ingredients);
        UserSteps userSteps = new UserSteps();
        OrderSteps orderSteps = new OrderSteps();

        userSteps.userCreate(userCreateRequest);
        orderSteps.orderCreate(orderCreateRequest)
                .assertThat().body("success", equalTo(true))
                .and()
                .assertThat().body("order.number", isA(Integer.class))
                .and()
                .statusCode(200);

    }

    @Test
    @DisplayName("Создание заказа после авторизации без ингредиентов")
    @Description("Проверка не возможности создания заказа после авторизации без ингредиентов")
    public void orderCreateWithAuthorizationWithoutIngredients() {

        UserCreateRequest userCreateRequest = new UserCreateRequest(Resources.email, Resources.password, Resources.name);
        UserLoginRequest userLoginRequest = new UserLoginRequest(Resources.email, Resources.password);
        OrderCreateRequest orderCreateRequest = new OrderCreateRequest(ingredients);
        UserSteps userSteps = new UserSteps();
        OrderSteps orderSteps = new OrderSteps();

        userSteps.userCreate(userCreateRequest);
        orderSteps.orderCreateAfterLogin(userLoginRequest, orderCreateRequest)
                .assertThat().body("success", equalTo(false))
                .and()
                .statusCode(400);

    }

    @Test
    @DisplayName("Создание заказа после авторизации с неверным ингредиентом")
    @Description("Проверка не возможности создания заказа после авторизации без ингредиентов")
    public void orderCreateWithAuthorizationWithWrongIngredients() {

        UserCreateRequest userCreateRequest = new UserCreateRequest(Resources.email, Resources.password, Resources.name);
        UserLoginRequest userLoginRequest = new UserLoginRequest(Resources.email, Resources.password);
        ingredients.add("61c0c5a71d1f82001bdaaa6d");
        ingredients.add("wrongIngredients");
        OrderCreateRequest orderCreateRequest = new OrderCreateRequest(ingredients);
        UserSteps userSteps = new UserSteps();
        OrderSteps orderSteps = new OrderSteps();

        userSteps.userCreate(userCreateRequest);
        orderSteps.orderCreateAfterLogin(userLoginRequest, orderCreateRequest)
                .assertThat().statusCode(500);

    }

}