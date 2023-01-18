import helpers.BaseClass;
import io.restassured.RestAssured;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class GetAllCharacters {
    public static final String baseUrl = "https://gateway.marvel.com:443/v1/public";

    @Test(testName = "Get all characters")
    public void getAllCharacters() {
        RestAssured.baseURI = baseUrl;
        given()
                .get(baseUrl + "/characters?ts=" + BaseClass.timestamp() + "&apikey=" + BaseClass.publicKey() + "&hash=" + BaseClass.hash() + "&limit=100")
                .then()
                .statusCode(200)
                .contentType("application/json; charset=utf-8")
                .body("data.count", equalTo(100))
                .body("data.total", equalTo(1562))
                .body("data.results", notNullValue())
                .log().all();
    }

    @Test(testName = "Authentication fails for missing parameter")
    public void authenticationFails() {
        RestAssured.baseURI = baseUrl;
        given()
                .get(baseUrl + "/characters?ts=" + BaseClass.timestamp() + "&apikey=" + BaseClass.publicKey())
                .then()
                .assertThat().body("code", is("MissingParameter")).and()
                .body("message", is("You must provide a hash."))
                .log().body();
    }

    @Test(testName = "Get more than a 100 characters")
    public void exceedCharactersLimit() {
        RestAssured.baseURI = baseUrl;
        given()
                .get(baseUrl + "/characters?ts=" + BaseClass.timestamp() + "&apikey=" + BaseClass.publicKey() + "&hash=" + BaseClass.hash() + "&limit=101")
                .then()
                .statusCode(409)
                .assertThat()
                .body("status", is("You may not request more than 100 items."))
                .log().body();
    }

    @Test(testName = "Get less than 1 character")
    public void fallBehindCharactersLimit() {
        RestAssured.baseURI = baseUrl;
        given()
                .get(baseUrl + "/characters?ts=" + BaseClass.timestamp() + "&apikey=" + BaseClass.publicKey() + "&hash=" + BaseClass.hash() + "&limit=0")
                .then()
                .statusCode(409)
                .assertThat()
                .body("status", is("You must pass an integer limit greater than 0."))
                .log().body();
    }
}
