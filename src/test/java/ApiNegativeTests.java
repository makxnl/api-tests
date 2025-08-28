import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


public class ApiNegativeTests {

    private static RequestSpecification requestSpec;
    private static ResponseSpecification code404ResponseSpec;

    private String wrongName = "abrakadabra";


    @BeforeAll
    static void init() {
        requestSpec = RestAssured.given()
                .baseUri("https://restcountries.com")
                .contentType("application/json");
        code404ResponseSpec = RestAssured.expect().statusCode(404);
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
