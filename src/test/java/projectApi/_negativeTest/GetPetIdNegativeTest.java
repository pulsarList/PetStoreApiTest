package projectApi._negativeTest;

import io.qameta.allure.Owner;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import projectApi.spec.Spec;

import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.is;

public class GetPetIdNegativeTest {

    @Test
    @Owner("Sergey")
    @DisplayName("Запрос на получение данных о питомце с несуществующим в DB id")
    public void getFindByIdUnknownId() {

        Spec.setSpec(Spec.reqSpec(JSON), Spec.resSpec(404));

        step("Request with unknown Id", () -> {
            given()
                    .pathParam("petId", 1010101010999111000L)
                    .formParam("name", "Sirius")
                    .formParam("status", "pending")
                    .when()
                    .get("/pet/{petId}")
                    .then()
                    .body("type", is("error"))
                    .body("message", is("Pet not found"));
        });
    }

}
