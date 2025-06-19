import api.methods.UserMethods;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.praktikum.page.object.models.User;
import ru.yandex.praktikum.page.object.models.UserLoginRequestBody;

import java.util.UUID;

import static org.hamcrest.Matchers.equalTo;

public class UpdateUserDataTests extends BaseUserTestsClass{

    private static final String ERROR_MESSAGE_UNAUTHORISED = "You should be authorised";

    private String auth;

    @Before
    @Override
    public void setup() {
        super.setup();
        auth = UserMethods.userCreate(new User(email, password, name)).body().path("accessToken");
    }

    @Test
    @DisplayName("Positive test: Update user's email")
    @Description("200 OK test for PATCH /api/auth/user with updated email in the request")
    public void updateEmailPositiveTest() {
        String newEmail = generateEmail();
        UserMethods.userDataUpdate(new User(newEmail, null, null), auth)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .assertThat().body("success", equalTo(true))
                .assertThat().body("user.email", equalTo(newEmail));
        email = newEmail;
    }

    @Test
    @DisplayName("Positive test: Update user's name")
    @Description("200 OK test for PATCH /api/auth/user with updated name in the request")
    public void updateNamePositiveTest() {
        String newName = UUID.randomUUID().toString();
        UserMethods.userDataUpdate(new User(null, null, newName), auth)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .assertThat().body("success", equalTo(true))
                .assertThat().body("user.name", equalTo(newName));
    }

    @Test
    @DisplayName("Positive test: Update user's password")
    @Description("200 OK test for PATCH /api/auth/user with updated password in the request")
    public void updatePasswordPositiveTest() {
        String newPassword = UUID.randomUUID().toString();
        UserMethods.userDataUpdate(new User(null, newPassword, null), auth)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .assertThat().body("success", equalTo(true));
        UserMethods.userLogin(new UserLoginRequestBody(email, newPassword))
                .then()
                .statusCode(HttpStatus.SC_OK);
        password = newPassword;
    }

    @Test
    @DisplayName("Negative test: Update user's email without authorization")
    @Description("401 UNAUTHORIZED test for PATCH /api/auth/user with updated email and without authorization in the request")
    public void updateEmailWithoutAuthNegativeTest() {
        String newEmail = generateEmail();
        UserMethods.userDataUpdate(new User(newEmail, null, null), "")
                .then()
                .statusCode(HttpStatus.SC_UNAUTHORIZED)
                .assertThat().body("success", equalTo(false))
                .assertThat().body("message", equalTo(ERROR_MESSAGE_UNAUTHORISED));
    }

    @Test
    @DisplayName("Negative test: Update user's name without authorization")
    @Description("401 UNAUTHORIZED test for PATCH /api/auth/user with updated name and without authorization in the request")
    public void updateNameWithoutAuthNegativeTest() {
        String newName = UUID.randomUUID().toString();
        UserMethods.userDataUpdate(new User(null, null, newName), "")
                .then()
                .statusCode(HttpStatus.SC_UNAUTHORIZED)
                .assertThat().body("success", equalTo(false))
                .assertThat().body("message", equalTo(ERROR_MESSAGE_UNAUTHORISED));
    }

    @Test
    @DisplayName("Negative test: Update user's password without authorization")
    @Description("401 UNAUTHORIZED test for PATCH /api/auth/user with updated password and without authorization in the request")
    public void updatePasswordWithoutAuthNegativeTest() {
        String newPassword = UUID.randomUUID().toString();
        UserMethods.userDataUpdate(new User(null, newPassword, null), "")
                .then()
                .statusCode(HttpStatus.SC_UNAUTHORIZED)
                .assertThat().body("success", equalTo(false))
                .assertThat().body("message", equalTo(ERROR_MESSAGE_UNAUTHORISED));
    }


}
