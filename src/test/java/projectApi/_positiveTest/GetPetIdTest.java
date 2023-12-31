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
import static io.restassured.http.ContentType.JSON;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class GetPetIdTest {

    private PetData data;
    private PetDataResponse newPet;

    @Test
    @Owner("Sergey")
    @DisplayName("Запрос на получение данных о питомце по id")
    public void getPetById() {

        Spec.setSpec(Spec.reqSpec(JSON), Spec.resSpec(200));

        data = new PetData();

        step("Filling the class model with data", () -> {

            data.setId(1L);

            String petName = new Faker().cat().name();
            data.setName(petName);

            ArrayList<String> photoUrl = new ArrayList<>();
            photoUrl.add("https://img.freepik.com/free-photo/kitty-with-monochrome-wall-behind-her_23-2148955134.jpg?w=360&t=st=1701017178~exp=1701017778~hmac=e6596400c9a8eb6b7fcc44392e58820bb3fd3999d45616b3e652bf0fe8b0652b");
            data.setPhotoUrls(photoUrl);

            data.setStatus("pending");
        });

        step("Create a new pet", () -> {
            newPet = PetApi.createANewPetAndExtractData(data);
        });

        PetDataResponse responseId =
                step("Request for checking new pet data", () -> {
                    return given()
                            .pathParam("petId", newPet.getId())
                            .when()
                            .get("/pet/{petId}")
                            .then()
                            .extract().as(PetDataResponse.class);
                });

        step("Checking that the returned response is not equal to NULL", () -> {
            assertNotNull(responseId);
        });

        step("Checking petId parameters", () -> {
            assertEquals(newPet.getId(), responseId.getId());
            assertEquals(newPet.getName(), responseId.getName());
            assertEquals(newPet.getStatus(), responseId.getStatus());
        });

    }

}
