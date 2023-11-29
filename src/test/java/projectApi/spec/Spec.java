package projectApi.spec;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import static io.restassured.filter.log.LogDetail.*;
import static projectApi.helpers.CustomApiListener.withCustomTemplates;

public class Spec {

    private final static String URL = "https://petstore.swagger.io";

    public static RequestSpecification reqSpec(ContentType contentType) {
        return new RequestSpecBuilder()
                .addFilter(withCustomTemplates())
                .log(ALL)
                .setBaseUri(URL)
                .setBasePath("/v2")
                .setContentType(contentType)
                .build();
    }

    public static ResponseSpecification resSpec(int expectStatusCode) {
        return new ResponseSpecBuilder()
                .log(ALL)
                .expectStatusCode(expectStatusCode)
                .build();
    }

    public static void setSpec(RequestSpecification request, ResponseSpecification response) {
        RestAssured.requestSpecification = request;
        RestAssured.responseSpecification = response;
    }

}
