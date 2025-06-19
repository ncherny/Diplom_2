import api.methods.UserMethods;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.apache.http.HttpStatus;
import org.junit.Test;
import ru.yandex.praktikum.page.object.models.User;

import static org.hamcrest.Matchers.equalTo;

public class RegisterUserTests extends BaseUserTestsClass{
    private static final String ERROR_MESSAGE_USER_EXISTS = "User already exists";
    private static final String ERROR_MESSAGE_MISSING_FIELD = "Email, password and name are required fields";

    @Test
    @DisplayName("Positive test: Register new user")
    @Description("200 OK test for POST /api/auth/register with valid data in the request")
    public void registerUserPositiveTest() {
        UserMethods.userCreate(new User(email, password, name))
                .then()
                .statusCode(HttpStatus.SC_OK)
                .assertThat().body("success", equalTo(true));
    }

    @Test
    @DisplayName("Negative test: Register an already existing user")
    @Description("403 FORBIDDEN test for POST /api/auth/register with using previously data of already existing user in the request")
    public void registerExistingUserNegativeTest() {
        User user = new User(email, password, name);
        UserMethods.userCreate(user);
        UserMethods.userCreate(user)
                .then()
                .statusCode(HttpStatus.SC_FORBIDDEN)
                .assertThat().body("success", equalTo(false))
                .assertThat().body("message", equalTo(ERROR_MESSAGE_USER_EXISTS));
    }

    @Test
    @DisplayName("Negative test: Register new user without email")
    @Description("401 UNAUTHORIZED test for POST /api/auth/register with missing email in the request")
    public void registerUserWithoutEmailNegativeTest() {
        UserMethods.userCreate(new User(null, password, name))
                .then()
                .statusCode(HttpStatus.SC_FORBIDDEN)
                .assertThat().body("success", equalTo(false))
                .assertThat().body("message", equalTo(ERROR_MESSAGE_MISSING_FIELD));
    }

    @Test
    @DisplayName("Negative test: Register new user without password")
    @Description("401 UNAUTHORIZED test for POST /api/auth/register with missing password in the request")
    public void registerUserWithoutPasswordNegativeTest() {
        UserMethods.userCreate(new User(email, null, name))
                .then()
                .statusCode(HttpStatus.SC_FORBIDDEN)
                .assertThat().body("success", equalTo(false))
                .assertThat().body("message", equalTo(ERROR_MESSAGE_MISSING_FIELD));
    }

    @Test
    @DisplayName("Negative test: Register new user without name")
    @Description("401 UNAUTHORIZED test for POST /api/auth/register with missing name in the request")
    public void registerUserWithoutNameNegativeTest() {
        UserMethods.userCreate(new User(email, password, null))
                .then()
                .statusCode(HttpStatus.SC_FORBIDDEN)
                .assertThat().body("success", equalTo(false))
                .assertThat().body("message", equalTo(ERROR_MESSAGE_MISSING_FIELD));
    }
}
