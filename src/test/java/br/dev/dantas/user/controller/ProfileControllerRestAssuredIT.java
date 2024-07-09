package br.dev.dantas.user.controller;

import static br.dev.dantas.user.controller.profilecontroller.IProfileController.V1_PATH_DEFAULT;

import br.dev.dantas.user.commons.FileUtils;
import br.dev.dantas.user.commons.ProfileUtils;
import br.dev.dantas.user.configuration.IntegrationTestContainers;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProfileControllerRestAssuredIT extends IntegrationTestContainers {

  @Autowired
  private ProfileUtils profileUtils;

  @Autowired
  private FileUtils fileUtils;

  @LocalServerPort
  private int port;

  @BeforeEach
  void setUrl() {
    RestAssured.baseURI = "http://localhost";
    RestAssured.port = port;
  }

  @Test
  @DisplayName("findAll() returns a list with all profiles")
  @Order(1)
  @Sql("/sql/init_two_profiles_rest_assured.sql")
  void findAll_ReturnsAllUsers_WhenSuccessful() throws Exception {
    var response = fileUtils.readResourceFile("profile/get-all-profile-rest-assured-200.json");

    RestAssured
        .given()
        .contentType(ContentType.JSON).accept(ContentType.JSON)
        .when()
        .get(V1_PATH_DEFAULT)
        .then()
        .statusCode(HttpStatus.OK.value())
        .log().all()
        .body("id", Matchers.hasItems(1, 2, 3))
        .body("name", Matchers.hasItems("admin", "develop", "test"))
        .body("description",Matchers.hasItems("profile role admin", "profile role develop", "profile role test"));
  }
}