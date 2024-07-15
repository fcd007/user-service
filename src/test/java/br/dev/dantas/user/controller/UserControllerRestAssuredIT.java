package br.dev.dantas.user.controller;

import static br.dev.dantas.user.controller.usercontroller.IUserController.V1_PATH_DEFAULT;

import br.dev.dantas.user.commons.FileUtils;
import br.dev.dantas.user.commons.UserUtils;
import br.dev.dantas.user.configuration.IntegrationTestContainers;
import br.dev.dantas.user.controller.profilecontroller.IProfileController;
import br.dev.dantas.user.repository.config.UserRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import net.javacrumbs.jsonunit.assertj.JsonAssertions;
import net.javacrumbs.jsonunit.core.Option;
import org.assertj.core.api.Assertions;
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
class UserControllerRestAssuredIT extends IntegrationTestContainers {

  @Autowired
  private UserUtils userUtils;

  @Autowired
  private FileUtils fileUtils;

  @Autowired
  private UserRepository userRepository;

  @LocalServerPort
  private int port;

  @BeforeEach
  void setUrl() {
    RestAssured.baseURI = "http://localhost";
    RestAssured.port = port;
  }

  @Test
  @DisplayName("findAll() returns a list with all users")
  @Order(1)
  void findAll_ReturnsAllEmpty_WhenNoUsersAreFound() throws Exception {
    var response = fileUtils.readResourceFile("user/get-all-users-empty-list-200.json");

    RestAssured
        .given()
        .contentType(ContentType.JSON).accept(ContentType.JSON)
        .when()
        .get(V1_PATH_DEFAULT)
        .then()
        .statusCode(HttpStatus.OK.value())
        .log().all()
        .body(Matchers.equalTo(response));
  }

  @Test
  @DisplayName("findById() returns all list when no users")
  @Order(2)
  @Sql("/sql/init_user_test_rest_assured.sql")
  void findById_ReturnsAllUsers_WhenSuccessful() throws Exception {
    var responseExpected = fileUtils.readResourceFile("user/get-user-by-id-200.json");

    var users = userRepository.findAll();
    Assertions.assertThat(users).hasSize(1);

    var response = RestAssured
        .given()
        .log().all()
        .contentType(ContentType.JSON).accept(ContentType.JSON)
        .when()
        .get(V1_PATH_DEFAULT + "/{id}", users.get(0).getId())
        .then()
        .statusCode(HttpStatus.OK.value())
        .log().all()
        .extract().response().body().asString();

    JsonAssertions.assertThatJson(response)
        .node("id")
        .asNumber()
        .isPositive();

    JsonAssertions.assertThatJson(response)
        .whenIgnoringPaths("id")
        .isEqualTo(responseExpected);

  }

  @Test
  @DisplayName("findAll() returns a list with all users")
  @Order(3)
  @Sql("/sql/init_three_users_rest_assured.sql")
  void findAll_ReturnsAllUsers_WhenSuccessful() throws Exception {
    var responseExpected = fileUtils.readResourceFile("user/get-all-users-200.json");

    var response = RestAssured
        .given()
        .contentType(ContentType.JSON).accept(ContentType.JSON)
        .when()
        .get(V1_PATH_DEFAULT)
        .then()
        .statusCode(HttpStatus.OK.value())
        .log().all()
        .extract().response().body().asString();

    JsonAssertions.assertThatJson(response)
        .and(users -> {
          users.node("[0].id").isNotNull();
          users.node("[1].id").isNotNull();
          users.node("[2].id").isNotNull();
        });

    JsonAssertions.assertThatJson(response)
        .whenIgnoringPaths("[*].id")
        .isEqualTo(responseExpected);
  }

  @Test
  @DisplayName("findById() returns a throw ResponseStatusException no user is found")
  @Order(4)
  void findById_ThrowResponseStatusException_WhenNoUserIsFound() throws Exception {
    var responseExpected = fileUtils.readResourceFile("user/get-user-by-id-400.json");

    var response = RestAssured
        .given()
        .contentType(ContentType.JSON).accept(ContentType.JSON)
        .log().all()
        .when()
        .get(V1_PATH_DEFAULT + "/{id}", 1999)
        .then()
        .statusCode(HttpStatus.NOT_FOUND.value())
        .log().all()
        .extract().response().body().asString();

    JsonAssertions.assertThatJson(response)
        .node("timestamp")
        .asString()
        .isNotEmpty();

    JsonAssertions.assertThatJson(response)
        .whenIgnoringPaths("timestamp")
        .when(Option.IGNORING_ARRAY_ORDER)
        .isEqualTo(responseExpected);
  }
}