package Order;

import User.*;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.core.Is.is;

public class OrderCreationTest {
    private User user = new User();

    private final UserSteps client = new UserSteps();

    private final OrderSteps orderSteps = new OrderSteps();

    private final UserGenerator generator = new UserGenerator();

    private List<String> ingredients = new ArrayList<>();;

    Order order;

    private String accessToken;



    @Before

    public void setUp() {

        user = generator.random();

        client.createUser(user);

    }



    @Test

    @DisplayName("Создание нового заказа")

    public void orderCreation() {

        ValidatableResponse loginResponse = client.loginUser(Credentials.from(user));

        accessToken = loginResponse.extract().path("accessToken").toString();

        ingredients = orderSteps.getIngredients().extract().path("data._id");

        order = new Order(ingredients);

        ValidatableResponse orderResponse = orderSteps.createOrder(order, accessToken);

        orderResponse
                .assertThat()
                .statusCode(200)
                .body("success", equalTo(true));

    }



    @Test

    @DisplayName("Создание заказа разлогиненным пользователем")

    public void orderCreationWithoutAuthorization() {

        ingredients = orderSteps.getIngredients().extract().path("data._id");
        order = new Order(ingredients);
        ValidatableResponse orderResponse = orderSteps.createOrderUnauthorizedUser(order);

        orderResponse
                .assertThat()
                .statusCode(200)
                .body("success", equalTo(true));

    }



    @Test

    @DisplayName("Создание заказа без ингредиентов")

    public void orderCreationWithoutIngredients() {

        ValidatableResponse loginResponse = client.loginUser(Credentials.from(user));
        accessToken = loginResponse.extract().path("accessToken").toString();
        order = new Order(ingredients);
        ValidatableResponse orderResponse = orderSteps.createOrder(order, accessToken);

        orderResponse
                .assertThat()
                .statusCode(400)
                .body("success", is(false))
                .body("message", equalTo("Ingredient ids must be provided"));

    }



    @Test

    @DisplayName("Создание заказа с неверным хешем ингредиентов")

    public void orderCreationWithWrongHashOfIngredients() {

        ValidatableResponse loginResponse = client.loginUser(Credentials.from(user));
        accessToken = loginResponse.extract().path("accessToken").toString();
        ingredients.add("60d3b41abdacab0026a733c6123456789");
        ingredients.add("60d3b41abdacab0026a733c6qwerty1234");
        order = new Order(ingredients);

        ValidatableResponse orderResponse = orderSteps.createOrder(order, accessToken);

        orderResponse
                .assertThat()
                .statusCode(500);

    }



    @After
    @DisplayName("Удаление пользователя")

    public void tearDown() {
        if (accessToken!=null) UserSteps.deleteUser(accessToken);
    }
}
