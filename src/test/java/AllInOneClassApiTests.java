import io.restassured.RestAssured;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class AllInOneClassApiTests {

    private static String baseUri = "https://restcountries.com/";
    private static ResponseSpecification code200ResponseSpec;
    private static ResponseSpecification code404ResponseSpec;
    private static RequestSpecification requestSpec;

    private String name = "russia";
    private String wrongName = "abrakadabra";


    @BeforeAll
    static void init() {
        code200ResponseSpec = new ResponseSpecBuilder().expectStatusCode(200).build();
        code404ResponseSpec = new ResponseSpecBuilder().expectStatusCode(404).build();
    }

    @BeforeEach
    void initRequestSpec() {
        requestSpec = RestAssured.given()
                .baseUri(baseUri)
                .contentType("application/json");
    }

    @Test
    @DisplayName("Код ответа 200, запрос russia возвращает список, содержащий страну Russia")
    void shouldBe200ResponseCodeAndCorrectCountryInList() {
        requestSpec
                .log().uri()
                .when()
                .get("/v3.1/name/{russia}", name)
                .then()
                .log().all()
                .spec(code200ResponseSpec)
                .body("[0].name.common", equalTo("Russia"))
                .body("[0].capital[0]", equalTo("Moscow"));
    }

    @Test
    @DisplayName("Код ответа 200, запрос russia возвращает список, содержащий страну Russia(через объект response)")
    void respObj() {
        Response response = RestAssured
                .given()
                .spec(requestSpec)
                .log().uri()
                .when()
                .get("/v3.1/name/{russia}", name)
                .then()
                .log().all()
                .spec(code200ResponseSpec)
                .extract().response();
        assertAll(
                () -> assertEquals("Russia", response.path("[0].name.common")),
                () -> assertEquals("Moscow", response.path("[0].capital[0]"))
        );
    }

    @Test
    @DisplayName("Код ответа 404, страна не найдена")
    void shouldBe404ResponseCodeAndCountryNotFound () {
        requestSpec
                .log().uri()
                .when()
                .get("/v3.1/name/{name}", wrongName)
                .then()
                .log().all()
                .spec(code404ResponseSpec);
    }

}
