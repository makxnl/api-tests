import io.restassured.RestAssured;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.equalTo;


public class AllInOneClassApiTests {

    private static String baseUri = "https://restcountries.com/";
    private static ResponseSpecification code200ResponseSpec;
    private static ResponseSpecification code404ResponseSpec;
    private RequestSpecification requestSpec = RestAssured.given()
            .baseUri(baseUri)
            .contentType("application/json");


    private String name = "russia";
    private String wrongName = "abrakadabra";

    @BeforeAll
    static void init() {
        code200ResponseSpec = new ResponseSpecBuilder().expectStatusCode(200).build();
        code404ResponseSpec = new ResponseSpecBuilder().expectStatusCode(404).build();
    }

    @Test
    @DisplayName("Код ответа 200, запрос russia возвращает список, содержащий страну Russia")
    void shouldBe200ResponseCodeAndCorrectCountryInList() {
        requestSpec
                .when()
                .get("/v3.1/name/{name}", name)
                .then()
                .spec(code200ResponseSpec)
                .body("[0].name.common", equalTo("Russia"))
                .body("[0].capital[0]", equalTo("Moscow"));
    }

    @Test
    @DisplayName("Код ответа 404, страна не найдена")
    void shouldBe404ResponseCodeAndCountryNotFound () {
        requestSpec
                .when()
                .get("/v3.1/name/{wrongName}", wrongName)
                .then()
                .spec(code404ResponseSpec);
    }
}
