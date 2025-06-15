import api.methods.UserMethods;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.praktikum.page.object.models.User;
import ru.yandex.praktikum.page.object.models.UserLoginRequestBody;
import util.Consts;

import java.util.UUID;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;


public class LoginUserTests extends BaseUserTestsClass {
    private static final String ERROR_MESSAGE_INCORRECT_DATA = "email or password are incorrect";
    private static final String ERROR_MESSAGE_MISSING_FIELD = "Email, password and name are required fields";

    @Before
    @Override
    public void setup() {
        super.setup();
        UserMethods.userCreate(new User(email, password, name));
    }

    @Test
    @DisplayName("Positive test: Login into user")
    public void loginUserPositiveTest() {
        UserMethods.userLogin(new UserLoginRequestBody(email, password))
                .then()
                .statusCode(HttpStatus.SC_OK)
                .assertThat().body("success", equalTo(true))
                .assertThat().body("accessToken", notNullValue())
                .assertThat().body("refreshToken", notNullValue());
    }

    @Test
    @DisplayName("Negative test: Login into user with invalid email")
    public void loginUserInvalidEmailNegativeTest() {
        UserMethods.userLogin(new UserLoginRequestBody(generateEmail(), password))
                .then()
                .statusCode(HttpStatus.SC_UNAUTHORIZED)
                .assertThat().body("success", equalTo(false))
                .assertThat().body("message", equalTo(ERROR_MESSAGE_INCORRECT_DATA));
    }

    @Test
    @DisplayName("Negative test: Login into user with invalid password")
    public void loginUserInvalidPasswordNegativeTest() {
        UserMethods.userLogin(new UserLoginRequestBody(email, UUID.randomUUID().toString()))
                .then()
                .statusCode(HttpStatus.SC_UNAUTHORIZED)
                .assertThat().body("success", equalTo(false))
                .assertThat().body("message", equalTo(ERROR_MESSAGE_INCORRECT_DATA));
    }
}
