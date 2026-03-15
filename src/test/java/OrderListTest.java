import org.Resources.Resources;
import org.steps.OrderSteps;
import org.steps.UserSteps;

import io.qameta.allure.junit4.DisplayName;
import jdk.jfr.Description;
import org.junit.After;
import org.junit.Test;
import org.pojo.UserCreateRequest;
import org.pojo.UserLoginRequest;

import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;

public class OrderListTest {

    @After
    public  void deleteUser() {
        UserSteps userSteps = new UserSteps();
        UserLoginRequest userLoginRequest = new UserLoginRequest(Resources.email, Resources.password);
        userSteps.userDeleteAfterLogin(userLoginRequest);
    }

    @Test
    @DisplayName("Получение списка заказов без авторизации")
    @Description("Проверка не возможности получения списка заказов без авторизации")
    public void orderListWithoutAuthorization() {

        OrderSteps orderSteps = new OrderSteps();

        orderSteps.orderList()
                .assertThat().body("success", equalTo(false))
                .and()
                .statusCode(401);
    }

    @Test
    @DisplayName("Получение списка заказов после авторизации")
    @Description("Проверка не возможности получения списка заказов после авторизации")
    public void orderListWithAuthorization() {

        UserCreateRequest userCreateRequest = new UserCreateRequest(Resources.email, Resources.password, Resources.name);
        UserLoginRequest userLoginRequest = new UserLoginRequest(Resources.email, Resources.password);
        UserSteps userSteps = new UserSteps();
        OrderSteps orderSteps = new OrderSteps();

        userSteps.userCreate(userCreateRequest);
        orderSteps.orderListAfterLogin(userLoginRequest)
                .assertThat().body("success", equalTo(true))
                .and()
                .assertThat().body("orders",instanceOf(List.class))
                .and()
                .statusCode(200);

    }

}
