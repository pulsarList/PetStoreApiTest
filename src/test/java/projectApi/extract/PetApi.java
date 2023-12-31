package projectApi.extract;

import projectApi.pojo.PetData;
import projectApi.pojo.PetDataResponse;
import projectApi.spec.Spec;

import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;

public class PetApi {

    public static PetDataResponse createANewPetAndExtractData(PetData petData) {
        PetDataResponse response;
        return response =
                step("Extracting the response into the model", () -> {
                    return given(Spec.reqSpec(JSON))
                            .body(petData)
                            .when()
                            .post("/pet")
                            .then()
                            .spec(Spec.resSpec(200))
                            .extract().as(PetDataResponse.class);
                });
    }
}
