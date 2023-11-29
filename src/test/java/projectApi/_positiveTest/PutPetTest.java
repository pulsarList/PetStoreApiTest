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
import static org.junit.jupiter.api.Assertions.*;


public class PutPetTest {

    private PetData data, putData;
    private PetDataResponse newPet;
    private String petName, petNameNew, petStatusNew;

    @Test
    @Owner("Sergey")
    @DisplayName("Обновление существующего в DB питомца")
    public void putPetById() {

        Spec.setSpec(Spec.reqSpec(JSON), Spec.resSpec(200));

        data = new PetData();
        putData = new PetData();

        step("Filling the class model with data", () -> {

            data.setId(98766789L);

            petName = new Faker().cat().name();
            data.setName(petName);

            ArrayList<String> photoUrl = new ArrayList<>();
            photoUrl.add("https://img.freepik.com/free-photo/kitty-with-monochrome-wall-behind-her_23-2148955134.jpg?w=360&t=st=1701017178~exp=1701017778~hmac=e6596400c9a8eb6b7fcc44392e58820bb3fd3999d45616b3e652bf0fe8b0652b");
            data.setPhotoUrls(photoUrl);

            data.setStatus("available");
        });

        step("Create a new pet", () -> {
            newPet = PetApi.createANewPetAndExtractData(data);
        });

        step("Validating newPet data", () -> {
            assertEquals(data.getId(), newPet.getId());
            assertEquals(data.getName(), newPet.getName());
            assertEquals(data.getPhotoUrls(), newPet.getPhotoUrls());
            assertEquals(data.getStatus(), newPet.getStatus());
        });

        step("Create a put data", () -> {
            petNameNew = new Faker().cat().name();
            petStatusNew = "pending";
            putData.setId(newPet.getId());
            putData.setName(petNameNew);
            putData.setStatus(petStatusNew);
        });

        PetDataResponse putResponse =
                step("Updating pet parameters", () -> {
                    return given()
                            .body(putData)
                            .when()
                            .put("/pet")
                            .then()
                            .extract().as(PetDataResponse.class);
                });

        step("Checking that the returned response is not equal to NULL", () -> {
            assertNotNull(putResponse);
        });

        step("Checking new parameters in response", () -> {
            assertEquals(putData.getId(), putResponse.getId());
            assertEquals(putData.getName(), putResponse.getName());
            assertEquals(putData.getStatus(), putResponse.getStatus());
            assertNull(putResponse.getPhotoUrls());
        });

    }
}
