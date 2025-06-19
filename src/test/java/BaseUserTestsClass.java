import api.methods.UserMethods;
import io.restassured.RestAssured;
import org.junit.After;
import org.junit.Before;
import ru.yandex.praktikum.page.object.models.UserLoginRequestBody;
import util.Consts;

import java.util.UUID;

public class BaseUserTestsClass {
    protected String email;
    protected String password;
    protected String name;

    private static final String EMAIL_PATTERN = "%s@example.com";

    @Before
    public void setup() {
        RestAssured.baseURI = Consts.BASE_URL;

        email = generateEmail();
        password = UUID.randomUUID().toString();
        name = UUID.randomUUID().toString();
    }

    @After
    public void tearDown() {
        UserMethods.cleanUpCreatedUser(new UserLoginRequestBody(email, password));
    }

    protected String generateEmail() {
        return String.format(EMAIL_PATTERN, UUID.randomUUID().toString());
    }
}
