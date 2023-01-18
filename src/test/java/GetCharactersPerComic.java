import helpers.BaseClass;
import io.restassured.RestAssured;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class GetCharactersPerComic {
    String baseUrl = "https://gateway.marvel.com:443/v1/public";
    static String comicID;

    @BeforeClass()
    public void getComicID() {
        RestAssured.baseURI = baseUrl;
        comicID =
                given()
                        .get(baseUrl + "/comics?title=X-Man&ts=" + BaseClass.timestamp() + "&apikey=" + BaseClass.publicKey() + "&hash=" + BaseClass.hash() + "&limit=100")
                        .then()
                        .extract()
                        .path("data.results[57].id").toString();
    }

    @Test(testName = "Get all characters of X-Man")
    public void getCharactersPerComic() {
        RestAssured.baseURI = baseUrl;
        given()
                .get(baseUrl + "/comics/" + comicID + "/characters?ts=" + BaseClass.timestamp() + "&apikey=" + BaseClass.publicKey() + "&hash=" + BaseClass.hash() + "&limit=100")
                .then()
                .statusCode(200)
                .contentType("application/json; charset=utf-8")
                .body("data.count", equalTo(10))
                .body("data.total", equalTo(10))
                .body("data.results", notNullValue())
                .body("data.results.size()", is(10))
                .body("data.limit", equalTo(100))
                .log().all();
    }

    @Test(testName = "Authentication fails for missing parameter")
    public void authenticationFails() {
        RestAssured.baseURI = baseUrl;
        given()
                .get(baseUrl + "/comics/" + comicID + "/characters?ts=" + BaseClass.timestamp() + " &apikey=" + BaseClass.publicKey())
                .then()
                .assertThat().body("code", is("MissingParameter")).and()
                .body("message", is("You must provide a hash."))
                .log().body();
    }

    @Test(testName = "Validate the default limit of characters")
    public void validateCharactersDefaultLimit() {
        RestAssured.baseURI = baseUrl;
        given()
                .get(baseUrl + "/comics/" + comicID + "/characters?ts=" + BaseClass.timestamp() + "&apikey=" + BaseClass.publicKey() + "&hash=" + BaseClass.hash())
                .then()
                .statusCode(200)
                .assertThat()
                .body("data.limit", is(20))
                .log().body();
    }

    @Test(testName = "Get more than 100 X-Man characters")
    public void exceedCharactersLimit() {
        RestAssured.baseURI = baseUrl;
        given()
                .get(baseUrl + "/comics/" + comicID + "/characters?ts=" + BaseClass.timestamp() + "&apikey=" + BaseClass.publicKey() + "&hash=" + BaseClass.hash() + "&limit=101")
                .then()
                .statusCode(409)
                .assertThat()
                .body("status", is("You may not request more than 100 items."))
                .log().body();
    }

    @Test(testName = "Get less than 1 X-Man character")
    public void fallBehindCharactersLimit() {
        RestAssured.baseURI = baseUrl;
        given()
                .get(baseUrl + "/comics/" + comicID + "/characters?ts=" + BaseClass.timestamp() + "&apikey=" + BaseClass.publicKey() + "&hash=" + BaseClass.hash() + "&limit=0")
                .then()
                .statusCode(409)
                .assertThat()
                .body("status", is("You must pass an integer limit greater than 0."))
                .log().body();
    }
}
