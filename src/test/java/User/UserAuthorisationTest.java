package User;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.equalTo;

public class UserAuthorisationTest {
    private User user = new User();

    private final UserSteps client = new UserSteps();

    private final UserGenerator generator = new UserGenerator();

    private String accessToken;



    @Before

    public void setUp() {

        user = generator.random();

    }



    @Test

    @DisplayName("Проверка авторизации пользователя")

    public void userAuthorization() {

        ValidatableResponse response = client.createUser(user);
        ValidatableResponse loginResponse = client.loginUser(Credentials.from(user));
        accessToken = loginResponse.extract().path("accessToken").toString();

        response

                .assertThat()
                .statusCode(200)
                .body("success", is(true));

    }



    @Test

    @DisplayName("Авторизация без поля email")

    public void userAuthorizationWithoutEmail() {

        user.setEmail("");
        ValidatableResponse response = client.loginUser(Credentials.from(user));

        response

                .assertThat()
                .statusCode(401)
                .body("success", is(false))
                .body("message", equalTo("email or password are incorrect"));

    }



    @Test

    @DisplayName("Авторизация без password невозможна")

    public void userAuthorizationWithoutPassword(){

        user.setPassword("");
        ValidatableResponse response = client.loginUser(Credentials.from(user));

        response
                .assertThat()
                .statusCode(401)
                .body("success", is(false))
                .body("message", equalTo("email or password are incorrect"));

    }

    @After
    @DisplayName("Удаление пользователя")

    public void tearDown() {
        if (accessToken!=null) UserSteps.deleteUser(accessToken);
    }
}
