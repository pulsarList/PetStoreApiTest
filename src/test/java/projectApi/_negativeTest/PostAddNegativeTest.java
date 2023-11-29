package projectApi._negativeTest;

import io.qameta.allure.Owner;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import projectApi.spec.Spec;

import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.is;

public class PostAddNegativeTest {

    @Test
    @Owner("Sergey")
    @DisplayName("Добавление нового питомца, без данных в теле запроса")
    public void postPetWithoutBody() {

        Spec.setSpec(Spec.reqSpec(JSON), Spec.resSpec(405));

        step("Request without body", () -> {
            given()
                    .when()
                    .post("/pet")
                    .then()
                    .body("", hasKey("message"))
                    .body("message", is("no data"));
        });
    }

}
