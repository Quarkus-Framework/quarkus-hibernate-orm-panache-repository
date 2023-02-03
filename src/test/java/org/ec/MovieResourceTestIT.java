package org.ec;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.*;

import javax.json.JsonObject;
import javax.json.Json;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.*;

@QuarkusTest
@Tag("integration")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class MovieResourceTestIT {

    @Test
    @Order(1)
    void getAll() {
        given()
                .when()
                .get("/movies")
                .then()
                .body("size()", equalTo(2))
                .body("id", hasItems(1, 2))
                .body("title", hasItems("FirstMovie", "SecondMovie"))
                .body("description", hasItems("MyFirstMovie", "MySecondMovie"))
                .body("director", hasItems("Me"))
                .body("country", hasItems("USA"))
                .statusCode(Response.Status.OK.getStatusCode());
    }

    @Test
    @Order(1)
    void getById() {
        given()
                .when()
                .get("/movies/1")
                .then()
                .body("id", equalTo(1))
                .body("title", equalTo("FirstMovie"))
                .body("description", equalTo("MyFirstMovie"))
                .body("director", equalTo("Me"))
                .body("country", equalTo("USA"))
                .statusCode(Response.Status.OK.getStatusCode());
    }

    @Test
    @Order(1)
    void getByIdKO() {
        given()
                .when()
                .get("/movies/3")
                .then()
                .statusCode(Response.Status.NOT_FOUND.getStatusCode());
    }

    @Test
    @Order(1)
    void getByTitle() {
        given()
                .when()
                .get("/movies/title/FirstMovie")
                .then()
                .body("id", equalTo(1))
                .body("title", equalTo("FirstMovie"))
                .body("description", equalTo("MyFirstMovie"))
                .body("director", equalTo("Me"))
                .body("country", equalTo("USA"))
                .statusCode(Response.Status.OK.getStatusCode());
    }

    @Test
    @Order(1)
    void getByTitleKO() {
        given()
                .when()
                .get("/movies/title/ThirdMovie")
                .then()
                .statusCode(Response.Status.NOT_FOUND.getStatusCode());
    }

    @Test
    @Order(1)
    void getByCountry() {
        given()
                .when()
                .get("/movies/country/USA")
                .then()
                .body("size()", equalTo(2))
                .body("id", hasItems(1, 2))
                .body("title", hasItems("FirstMovie", "SecondMovie"))
                .body("description", hasItems("MyFirstMovie", "MySecondMovie"))
                .body("director", hasItems("Me"))
                .body("country", hasItems("USA"))
                .statusCode(Response.Status.OK.getStatusCode());
    }

    @Test
    @Order(1)
    void getByCountryKO() {
        given()
                .when()
                .get("/movies/country/France")
                .then()
                .body("size()", equalTo(0))
                .statusCode(Response.Status.OK.getStatusCode());
    }

    @Test
    @Order(2)
    void create() {
        JsonObject jsonObject = Json.createObjectBuilder()
                .add("title", "ThirdMovie")
                .add("description", "MyThirdMovie")
                .add("director", "me")
                .add("country", "USA")
                .build();

        given()
                .contentType(MediaType.APPLICATION_JSON)
                .body(jsonObject.toString())
                .when()
                .post("/movies")
                .then()
                .statusCode(Response.Status.CREATED.getStatusCode());

    }

    @Test
    @Order(3)
    void updateById() {
        JsonObject jsonObject = Json.createObjectBuilder()
                .add("title", "SecondMovieUpdated").build();

        given()
            .contentType(MediaType.APPLICATION_JSON)
                .body(jsonObject.toString())
                .when()
                .put("/movies/2")
                .then()
                .body("id", equalTo(2))
                .body("title", equalTo("SecondMovieUpdated"))
                .body("description", equalTo("MySecondMovie"))
                .body("director", equalTo("Me"))
                .body("country", equalTo("USA"))
                .statusCode(Response.Status.OK.getStatusCode());
    }

    @Test
    @Order(3)
    void updateByIdKO() {

        JsonObject jsonObject = Json.createObjectBuilder()
                .add("title", "SecondMovieUpdated").build();

        given()
                .contentType(MediaType.APPLICATION_JSON)
                .body(jsonObject.toString())
                .when()
                .put("/movies/2222")
                .then()
                .statusCode(Response.Status.NOT_FOUND.getStatusCode());
    }

    @Test
    @Order(4)
    void deleteById() {
        given()
                .when()
                .delete("/movies/2")
                .then()
                .statusCode(Response.Status.OK.getStatusCode());

        given()
                .when()
                .get("/movies/2")
                .then()
                .statusCode(Response.Status.NOT_FOUND.getStatusCode());
    }

    @Test
    @Order(4)
    void deleteByIdKO() {
        given()
                .when()
                .delete("/movies/2222")
                .then()
                .statusCode(Response.Status.NOT_FOUND.getStatusCode());
    }
}