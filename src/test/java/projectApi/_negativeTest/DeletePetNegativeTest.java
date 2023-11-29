package projectApi._negativeTest;

import io.qameta.allure.Owner;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import projectApi.spec.Spec;

import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;

public class DeletePetNegativeTest {

    @Test
    @Owner("Sergey")
    @DisplayName("Удаление питомца по несуществующему в DB id")
    public void deletingAPetForAnIDThatDoesNotExistInDB() {

        Spec.setSpec(Spec.reqSpec(JSON), Spec.resSpec(404));

        step("Delete pet is not exist in DB", () -> {
            given()
                    .pathParam("petId", 90009000009L)
                    .when()
                    .delete("/pet/{petId}")
                    .then();
        });
    }

}
