package projectApi._negativeTest;

import com.github.javafaker.Faker;
import io.qameta.allure.Owner;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import projectApi.pojo.PetData;
import projectApi.spec.Spec;

import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.is;

public class PutPetNegativeTest {

    private PetData putData;
    private String petNameNew, petStatusNew;

    @Test
    @Owner("Sergey")
    @DisplayName("Ошибка при попытке обновить несуществующее домашнее животное")
    @Disabled("Некорректная работа сервиса: status 200 вместо 404")
    public void putPetInExist() {
        putData = new PetData();

        Long existId = 1010101010999L;

        step("Request with unknown Id", () -> {
            given(Spec.reqSpec(JSON))
                    .pathParam("petId", existId)
                    .formParam("name", "Sirius")
                    .formParam("status", "pending")
                    .when()
                    .get("/pet/{petId}")
                    .then()
                    .spec(Spec.resSpec(404))
                    .body("type", is("error"))
                    .body("message", is("Pet not found"));
        });

        step("Create a put data", () -> {
            petNameNew = new Faker().cat().name();
            petStatusNew = "pending";
            putData.setId(existId);
            putData.setName(petNameNew);
            putData.setStatus(petStatusNew);
        });

        step("Sending request with not exist Id", () -> {
            given(Spec.reqSpec(JSON))
                    .body(putData)
                    .when()
                    .put("/pet")
                    .then()
                    .spec(Spec.resSpec(404));
        });
    }

}
