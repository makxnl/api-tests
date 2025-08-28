import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.equalTo;

public class ApiPositiveTests {

    private static RequestSpecification requestSpec;
    private static ResponseSpecification code200ResponseSpec;

    private String name = "russia";


    @BeforeAll
    static void init() {
        requestSpec = RestAssured.given()
                .baseUri("https://restcountries.com")
                .contentType("application/json");
        code200ResponseSpec = RestAssured.expect().statusCode(200);
    }

    @Test
    @DisplayName("Код ответа 200, возвращяется корректная страна и её столица")
    void shouldBe200ResponseCodeAndCorrectCountryAndCapital() {
        requestSpec
                .when()
                .get("/v3.1/name/{name}", name)
                .then()
                .spec(code200ResponseSpec)
                .body("[0].name.common", equalTo("Russia"))
                .body("[0].capital[0]", equalTo("Moscow"));

    }

}
