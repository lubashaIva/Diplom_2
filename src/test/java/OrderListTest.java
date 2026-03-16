import com.github.javafaker.Faker;
import org.junit.Before;
import org.steps.OrderSteps;
import org.steps.UserSteps;

import io.qameta.allure.junit4.DisplayName;
import jdk.jfr.Description;
import org.junit.After;
import org.junit.Test;
import org.pojo.UserCreateRequest;
import org.pojo.UserLoginRequest;

import java.util.List;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;

public class OrderListTest {

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
    @DisplayName("Получение списка заказов без авторизации")
    @Description("Проверка не возможности получения списка заказов без авторизации")
    public void orderListWithoutAuthorization() {

        OrderSteps orderSteps = new OrderSteps();

        orderSteps.orderList()
                .statusCode(SC_UNAUTHORIZED)
                .and()
                .assertThat().body("success", equalTo(false));
    }

    @Test
    @DisplayName("Получение списка заказов после авторизации")
    @Description("Проверка не возможности получения списка заказов после авторизации")
    public void orderListWithAuthorization() {

        UserCreateRequest userCreateRequest = new UserCreateRequest(email, password, name);
        UserLoginRequest userLoginRequest = new UserLoginRequest(email, password);
        UserSteps userSteps = new UserSteps();
        OrderSteps orderSteps = new OrderSteps();

        userSteps.userCreate(userCreateRequest);
        orderSteps.orderListAfterLogin(userLoginRequest)
                .statusCode(SC_OK)
                .and()
                .assertThat().body("success", equalTo(true))
                .and()
                .assertThat().body("orders",instanceOf(List.class));

    }

}
