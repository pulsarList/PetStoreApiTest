package projectApi._negativeTest;

import com.github.javafaker.Faker;
import io.qameta.allure.Owner;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import projectApi.spec.Spec;

import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.URLENC;
import static org.hamcrest.Matchers.is;

public class PostUpdateNegativeTest {

    private Long petId;
    private String petNameNew, petStatusNew;

    @Test
    @Owner("Sergey")
    @DisplayName("Обновление данных питомца с несуществующим в DB id")
    public void postPetIdWithoutFormData() {

        Spec.setSpec(Spec.reqSpec(URLENC), Spec.resSpec(404));

        step("Create a update data", () -> {
            petId = 777770077777L;
            petNameNew = new Faker().cat().name();
            petStatusNew = "available";
        });

        step("Updating pet info with form data in invalid Id", () -> {
            given()
                    .pathParam("petId", petId)
                    .formParam("name", petNameNew)
                    .formParam("status", petStatusNew)
                    .when()
                    .post("/pet/{petId}")
                    .then()
                    .body("type", is("unknown"))
                    .body("message", is("not found"));
        });
    }

}
