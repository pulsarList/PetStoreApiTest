package projectApi._positiveTest;

import com.github.javafaker.Faker;
import io.qameta.allure.*;
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
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class DeletePetTest {

    private PetData data;
    private PetDataResponse newPet;

    Long petId;

    @Test
    @Owner("Sergey")
    @DisplayName("Удаление питомца по id и проверка, что данный питомец удалён из BD")
    public void deletePetById() {

        Spec.setSpec(Spec.reqSpec(JSON), Spec.resSpec(200));

        data = new PetData();

        step("Filling the class model with data", () -> {

            data.setId(88888888L);

            String petName = new Faker().cat().name();
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

        step("Validating newPet data", () -> {
            assertEquals(data.getName(), newPet.getName());
            assertEquals(data.getPhotoUrls(), newPet.getPhotoUrls());
            assertEquals(data.getStatus(), newPet.getStatus());
        });

        step("Request to delete created pet", () -> {
            given()
                    .pathParam("petId", petId)
                    .when()
                    .delete("/pet/{petId}")
                    .then()
                    .body("type", is("unknown"))
                    .body("message", is(newPet.getId().toString()));
        });

        Spec.setSpec(Spec.reqSpec(JSON), Spec.resSpec(404));
        step("Checking pet is not exist in DB", () -> {
            given()
                    .pathParam("petId", petId)
                    .when()
                    .get("/pet/{petId}")
                    .then()
                    .body("type", is("error"))
                    .body("message", is("Pet not found"));
        });
    }
}
