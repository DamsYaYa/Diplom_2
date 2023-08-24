package User;

import Constant.Base;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import static Constant.Endpoints.*;

public class UserSteps extends Base {


    @Step("Регистрация пользователя")

    public ValidatableResponse createUser(User user) {

        return spec()
                .body(user)
                .when()
                .post(NEW_USER_API)
                .then().log().all();
    }



    @Step("Авторизация пользователя")

    public ValidatableResponse loginUser(Credentials credentials) {

        return spec()
                .body(credentials)
                .when()
                .post(LOGIN_API)
                .then().log().all();
    }



    @Step("Удаление пользователя")

    public static ValidatableResponse deleteUser(String accessToken) {

        return spec()
                .header("Authorization", accessToken)
                .when()
                .delete(AUTH_USER_API)
                .then();
    }



    @Step("Изменение пользователя c авторизацией")

    public ValidatableResponse updateUser(User user, String accessToken) {

        return spec()
                .header("Authorization", accessToken)
                .body(user)
                .when()
                .patch(AUTH_USER_API)
                .then().log().all();
    }



    @Step("Изменение пользователя без авторизации")

    public ValidatableResponse updateUserWithoutLogin(User user) {

        return spec()
                .body(user)
                .when()
                .patch(AUTH_USER_API)
                .then().log().all();
    }

}
