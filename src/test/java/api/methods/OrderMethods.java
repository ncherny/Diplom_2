package api.methods;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import ru.yandex.praktikum.page.object.models.CreateOrderRequestBody;

import static io.restassured.RestAssured.given;

public class OrderMethods {
    private static final String URL_ORDER_CREATE = "/api/orders";
    private static final String URL_GET_USER_ORDERS = "/api/orders";
    private static final String URL_GET_INGREDIENTS_DATA = "/api/ingredients";

    @Step("Create order")
    public static Response orderCreate(CreateOrderRequestBody createOrderRequestBody, String auth) {
        return given()
                .header("Authorization", auth)
                .header("Content-type", "application/json")
                .and()
                .body(createOrderRequestBody)
                .when()
                .post(URL_ORDER_CREATE);
    }

    @Step("Get list of ingredients")
    public static Response getIngredients() {
        return given()
                .when()
                .get(URL_GET_INGREDIENTS_DATA);
    }

    @Step("Get list of user's orders")
    public static Response getUserOrders(String auth) {
        return given()
                .header("Authorization", auth)
                .when()
                .get(URL_GET_USER_ORDERS);
    }

    @Step("Get sample ingredient from ingredient's list")
    public static String getSampleIngredient() {
        return getIngredients().body().path("data[0]._id");
    }

}
