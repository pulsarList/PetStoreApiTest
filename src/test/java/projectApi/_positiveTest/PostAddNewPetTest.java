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
import static io.restassured.http.ContentType.JSON;
import static org.junit.jupiter.api.Assertions.*;

public class PostAddNewPetTest {

    private PetData data;
    private PetDataResponse newPet;

    @Test
    @Owner("Sergey")
    @DisplayName("Добавление нового питомца в магазин")
    public void addPetCorrectData() {

        Spec.setSpec(Spec.reqSpec(JSON), Spec.resSpec(200));

        data = new PetData();

        step("Filling the class model with data", () -> {
            data.setId(1L);
            data.setCategory(new PetData.Category(8, "Cat"));

            String petName = new Faker().cat().name();
            data.setName(petName);

            ArrayList<String> photoUrl = new ArrayList<>();
            photoUrl.add("https://img.freepik.com/free-photo/kitty-with-monochrome-wall-behind-her_23-2148955134.jpg?w=360&t=st=1701017178~exp=1701017778~hmac=e6596400c9a8eb6b7fcc44392e58820bb3fd3999d45616b3e652bf0fe8b0652b");
            data.setPhotoUrls(photoUrl);

            PetData.Tag tag = new PetData.Tag();
            tag.id = 22;
            tag.name = "adult";
            ArrayList<PetData.Tag> tags = new ArrayList<>();
            tags.add(tag);
            data.setTags(tags);

            data.setStatus("pending");
        });

        step("Create a new pet", () -> {
            newPet = PetApi.createANewPetAndExtractData(data);
        });

        step("Checking that the returned response is not equal to NULL", () -> {
            assertNotNull(newPet);
        });

        step("Validating pet data", () -> {
            assertEquals(data.getId(), newPet.getId());
            assertEquals(data.getCategory().id, newPet.getCategory().id);
            assertEquals(data.getCategory().name, newPet.getCategory().name);
            assertEquals(data.getName(), newPet.getName());
            assertEquals(data.getPhotoUrls(), newPet.getPhotoUrls());
            assertEquals(data.getStatus(), newPet.getStatus());
            assertEquals(data.getTags().get(0).id, newPet.getTags().get(0).id);
            assertEquals(data.getTags().get(0).name, newPet.getTags().get(0).name);
        });
    }

}
