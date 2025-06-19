import api.methods.OrderMethods;
import api.methods.UserMethods;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.praktikum.page.object.models.User;


import static org.hamcrest.Matchers.equalTo;

public class GetOrderByUserTests extends BaseUserTestsClass {
    private String auth;

    private static final String ERROR_MESSAGE_UNAUTHORISED = "You should be authorised";

    @Before
    @Override
    public void setup() {
        super.setup();
        auth = UserMethods.userCreate(new User(email, password, name))
                .body().path("accessToken");;
    }

    @Test
    @DisplayName("Positive test: Get user's order")
    @Description("200 OK test for GET /api/orders with valid data in the request")
    public void getUserOrdersPositiveTest() {
        OrderMethods.getUserOrders(auth)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .assertThat().body("success", equalTo(true))
                .assertThat().body("orders", Matchers.notNullValue());
    }

    @Test
    @DisplayName("Negative test: Get user's order without authorization")
    @Description("401 UNAUTHORIZED test for GET /api/orders without authorization in the request")
    public void getUserOrdersMissingAuthNegativeTest() {
        OrderMethods.getUserOrders("")
                .then()
                .statusCode(HttpStatus.SC_UNAUTHORIZED)
                .assertThat().body("success", equalTo(false))
                .assertThat().body("message", equalTo(ERROR_MESSAGE_UNAUTHORISED));
    }

}
