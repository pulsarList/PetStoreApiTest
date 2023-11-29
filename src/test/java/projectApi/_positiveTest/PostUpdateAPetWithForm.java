package projectApi._positiveTest;

import com.github.javafaker.Faker;
import io.qameta.allure.Owner;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import projectApi.extract.PetApi;
import projectApi.pojo.PetData;
import projectApi.pojo.PetDataResponse;
import projectApi.spec.Spec;

import java.util.ArrayList;

import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.*;
import static org.junit.jupiter.api.Assertions.*;

public class PostUpdateAPetWithForm {

    private PetData data;
    private PetDataResponse newPet;
    private Long petId;
    private String petName, petNameNew, petStatusNew;

    @Test
    @Owner("Sergey")
    @DisplayName("Обновление имени и статуса питомца")
    public void updatesAPetInTheStoreWithFormData() {

        Spec.setSpec(Spec.reqSpec(URLENC), Spec.resSpec(200));

        data = new PetData();

        step("Filling the class model with data", () -> {

            data.setId(12366L);

            petName = new Faker().cat().name();
            data.setName(petName);

            ArrayList<String> photoUrl = new ArrayList<>();
            photoUrl.add("https://img.freepik.com/free-photo/kitty-with-monochrome-wall-behind-her_23-2148955134.jpg?w=360&t=st=1701017178~exp=1701017778~hmac=e6596400c9a8eb6b7fcc44392e58820bb3fd3999d45616b3e652bf0fe8b0652b");
            data.setPhotoUrls(photoUrl);

            data.setStatus("pending");
        });

        step("Create a new pet", () -> {
            newPet = PetApi.createANewPetAndExtractData(data);
            petId = newPet.getId();
        });

        step("Create a update data", () -> {
            petNameNew = new Faker().cat().name();
            petStatusNew = "available";
        });

        step("Updating pet info with form data", () -> {
            given(Spec.reqSpec(URLENC))
                    .pathParam("petId", petId)
                    .formParam("name", petNameNew)
                    .formParam("status", petStatusNew)
                    .when()
                    .post("/pet/{petId}")
                    .then();
        });

        PetDataResponse responseId =
                step("Request for checking new pet data", () -> {
                    return given()
                            .pathParam("petId", petId)
                            .when()
                            .get("/pet/{petId}")
                            .then()
                            .extract().as(PetDataResponse.class);
                });

        step("Checking that the returned response is not equal to NULL", () -> {
            assertNotNull(responseId);
        });

        step("Checking new parameters", () -> {
            assertEquals(petId, responseId.getId());
            assertEquals(petNameNew, responseId.getName());
            assertEquals(petStatusNew, responseId.getStatus());
        });
    }

}
