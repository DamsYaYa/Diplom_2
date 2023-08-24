package Order;

import Constant.Base;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import static Constant.Endpoints.*;

public class OrderSteps extends Base {

    @Step("Создание заказа авторизованным пользователем")

    public ValidatableResponse createOrder(Order order, String accessToken) {

        return  spec()
                .header("Authorization", accessToken)
                .body(order)
                .when()
                .post(ORDERS_API)
                .then().log().all();
    }


    @Step("Создание заказа неавторизованным пользователем")

    public ValidatableResponse createOrderUnauthorizedUser(Order order) {

        return  spec()
                .body(order)
                .when()
                .post(ORDERS_API)
                .then().log().all();
    }


    @Step("Получение ингредиентов")

    public ValidatableResponse getIngredients() {

        return spec()
                .log().uri()
                .when()
                .get(INGREDIENTS_API)
                .then().log().all();
    }


    @Step("Получение заказов авторизованного пользователя")

    public ValidatableResponse getOrdersOfAuthorizedUser(String accessToken) {

        return spec()
                .header("Authorization", accessToken)
                .get(ORDERS_API)
                .then().log().all();
    }

    @Step("Получение заказов неавторизованного пользователя")

    public ValidatableResponse getOrdersOfUnauthorizedUser() {

        return spec()
                .get(ORDERS_API)
                .then().log().all();
    }

}
