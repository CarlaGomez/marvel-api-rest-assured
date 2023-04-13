import helpers.BaseClass;
import io.restassured.RestAssured;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class GetComicsPerCharacter {
    static String characterID;
    String baseUrl = "https://gateway.marvel.com:443/v1/public";

    @BeforeClass()
    public void getCharacterID() {
        RestAssured.baseURI = baseUrl;
        characterID =
                given()
                        .get(baseUrl + "/characters?nameStartsWith=Spider-Man&ts=" + BaseClass.timestamp() + "&apikey=" + BaseClass.publicKey() + "&hash=" + BaseClass.hash() + "&limit=100")
                        .then()
                        .extract()
                        .path("data.results[10].id").toString();
    }

    @Test(testName = "Get Spider-Man comics")
    public void getComicsPerCharacter() {
        RestAssured.baseURI = baseUrl;
        given()
                .get(baseUrl + "/characters/" + characterID + "/comics?ts=" + BaseClass.timestamp() + "&apikey=" + BaseClass.publicKey() + "&hash=" + BaseClass.hash() + "&limit=100")
                .then()
                .statusCode(200)
                .contentType("application/json; charset=utf-8")
                .body("data.count", equalTo(100))
                .body("data.total", equalTo(4223))
                .body("data.results", notNullValue())
                .body("data.results.size()", is(100))
                .body("data.limit", equalTo(100))
                .log().all();
    }

    @Test(testName = "Authentication fails for missing parameter")
    public void authenticationFails() {
        RestAssured.baseURI = baseUrl;
        given()
                .get(baseUrl + "/characters/" + characterID + "/comics?ts=" + BaseClass.timestamp() + " &apikey=" + BaseClass.publicKey())
                .then()
                .assertThat().body("code", is("MissingParameter")).and()
                .body("message", is("You must provide a hash."))
                .log().body();
    }

    @Test(testName = "Validate the default limit of comics")
    public void validateComicsDefaultLimit() {
        RestAssured.baseURI = baseUrl;
        given()
                .get(baseUrl + "/characters/" + characterID + "/comics?ts=" + BaseClass.timestamp() + "&apikey=" + BaseClass.publicKey() + "&hash=" + BaseClass.hash())
                .then()
                .statusCode(200)
                .assertThat()
                .body("data.limit", is(20))
                .log().body();
    }

    @Test(testName = "Get more than 100 Spider-Man comics")
    public void exceedComicsLimit() {
        RestAssured.baseURI = baseUrl;
        given()
                .get(baseUrl + "/characters/" + characterID + "/comics?ts=" + BaseClass.timestamp() + "&apikey=" + BaseClass.publicKey() + "&hash=" + BaseClass.hash() + "&limit=101")
                .then()
                .statusCode(409)
                .assertThat()
                .body("status", is("You may not request more than 100 items."))
                .log().body();
    }

    @Test(testName = "Get less than 1 Spider-Man comic")
    public void fallBehindComicsLimit() {
        RestAssured.baseURI = baseUrl;
        given()
                .get(baseUrl + "/characters/" + characterID + "/comics?ts=" + BaseClass.timestamp() + "&apikey=" + BaseClass.publicKey() + "&hash=" + BaseClass.hash() + "&limit=0")
                .then()
                .statusCode(409)
                .assertThat()
                .body("status", is("You must pass an integer limit greater than 0."))
                .log().body();
    }
}

