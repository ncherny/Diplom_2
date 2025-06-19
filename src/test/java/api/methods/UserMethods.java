package api.methods;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import ru.yandex.praktikum.page.object.models.User;
import ru.yandex.praktikum.page.object.models.UserLoginRequestBody;

import static io.restassured.RestAssured.given;

public class UserMethods {

    private static final String URL_USER_CREATE = "/api/auth/register";
    private static final String URL_USER_LOGIN = "/api/auth/login";
    private static final String URL_USER_DATA_UPDATE = "/api/auth/user";
    private static final String URL_USER_DELETE = "/api/auth/user";

    @Step("Create user")
    public static Response userCreate(User user) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(user)
                .when()
                .post(URL_USER_CREATE);
    }

    @Step("Login into user's account")
    public static Response userLogin(UserLoginRequestBody userLoginRequestBody) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(userLoginRequestBody)
                .when()
                .post(URL_USER_LOGIN);
    }

    @Step("Update user's data")
    public static Response userDataUpdate(User user, String auth) {
        return given()
                .header("Authorization", auth)
                .header("Content-type", "application/json")
                .and()
                .body(user)
                .when()
                .patch(URL_USER_DATA_UPDATE);
    }

    @Step("Delete user")
    public static Response userDelete (String auth) {
        return given()
                .header("Authorization", auth)
                .and()
                .when()
                .delete(URL_USER_DELETE);
    }

    @Step("Clean up previously created user")
    public static void cleanUpCreatedUser(UserLoginRequestBody user) {
        Response loginResponse = userLogin(user);
        if (loginResponse.contentType().equals("application/json")) {
            String auth = loginResponse.body().path("accessToken");
            if (auth != null) {
                userDelete(auth);
            }
        }
    }
}
