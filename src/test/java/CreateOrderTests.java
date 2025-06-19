import api.methods.OrderMethods;
import api.methods.UserMethods;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.praktikum.page.object.models.CreateOrderRequestBody;
import ru.yandex.praktikum.page.object.models.User;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.equalTo;

public class CreateOrderTests extends BaseUserTestsClass {

    private List<String> ingredients;
    private String auth;

    private static final String ERROR_MESSAGE_UNAUTHORISED = "You should be authorised";
    private static final String ERROR_MESSAGE_MISSING_INGREDIENTS = "Ingredient ids must be provided";

    @Before
    @Override
    public void setup() {
        super.setup();
        auth = UserMethods.userCreate(new User(email, password, name)).body().path("accessToken");
    }

    @Test
    @DisplayName("Positive test: Create order")
    @Description("200 OK test for POST /api/orders with valid data in the request")
    public void createOrderPositiveTest() {
        ingredients = List.of(OrderMethods.getSampleIngredient());
        OrderMethods.orderCreate(new CreateOrderRequestBody(ingredients), auth)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .assertThat().body("success", equalTo(true));
    }

    @Test
    @DisplayName("Negative test: Create order without authorization")
    @Description("401 UNAUTHORIZED test for POST /api/orders with missing authorization in the request")
    public void createOrderNoAuthNegativeTest() {
        ingredients = List.of(OrderMethods.getSampleIngredient());
        OrderMethods.orderCreate(new CreateOrderRequestBody(ingredients), "")
                .then()
                .statusCode(HttpStatus.SC_UNAUTHORIZED)
                .assertThat().body("success", equalTo(false))
                .assertThat().body("message", equalTo(ERROR_MESSAGE_UNAUTHORISED));
    }

    @Test
    @DisplayName("Negative test: Create order with empty ingredients field")
    @Description("400 BAD REQUEST test for POST /api/orders with empty ingredients array in the request")
    public void createOrderNoIngredientsNegativeTest() {
        OrderMethods.orderCreate(new CreateOrderRequestBody(new ArrayList<>()), auth)
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .assertThat().body("success", equalTo(false))
                .assertThat().body("message", equalTo(ERROR_MESSAGE_MISSING_INGREDIENTS));
    }

    @Test
    @DisplayName("Negative test: Create order without ingredients field")
    @Description("400 BAD REQUEST test for POST /api/orders without ingredients field in the request")
    public void createOrderMissingIngredientsFieldNegativeTest() {
        OrderMethods.orderCreate(new CreateOrderRequestBody(null), auth)
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .assertThat().body("success", equalTo(false))
                .assertThat().body("message", equalTo(ERROR_MESSAGE_MISSING_INGREDIENTS));
    }

    @Test
    @DisplayName("Negative test: Create order with an invalid ingredient id")
    @Description("500 KO test for POST /api/orders with invalid ingredient id in the request")
    public void createOrderInvalidIngredientNegativeTest() {
        OrderMethods.orderCreate(new CreateOrderRequestBody(List.of(UUID.randomUUID().toString())), auth)
                .then()
                .statusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
    }
}
