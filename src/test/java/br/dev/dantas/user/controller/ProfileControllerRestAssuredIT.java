package br.dev.dantas.user.controller;

import static br.dev.dantas.user.controller.profilecontroller.IProfileController.V1_PATH_DEFAULT;

import br.dev.dantas.user.commons.FileUtils;
import br.dev.dantas.user.commons.ProfileUtils;
import br.dev.dantas.user.configuration.IntegrationTestContainers;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.stream.Stream;
import net.javacrumbs.jsonunit.assertj.JsonAssertions;
import net.javacrumbs.jsonunit.core.Option;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
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
  @DisplayName("findAll() returns a list empty when no profiles are found")
  @Order(1)
  @Sql("/sql/reset_two_profiles_rest_assured.sql")
  void findAll_ReturnsAllEmpty_WhenNoUsersAreFound() throws Exception {
    var response = fileUtils.readResourceFile("profile/get-all-profiles-empty-list-200.json");

    RestAssured
        .given()
        .contentType(ContentType.JSON).accept(ContentType.JSON)
        .log().all()
        .when()
        .get(V1_PATH_DEFAULT)
        .then()
        .statusCode(HttpStatus.OK.value())
        .log().all()
        .body(Matchers.equalTo(response));
  }

  @Test
  @DisplayName("findAll() returns a list with all profiles")
  @Order(2)
  @Sql("/sql/init_two_profiles_rest_assured.sql")
  void findAll_ReturnsAllUsers_WhenSuccessful() throws Exception {

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
        .body("description",
            Matchers.hasItems("profile role admin", "profile role develop", "profile role test"));
  }

  @Test
  @DisplayName("save() create a profile")
  @Order(3)
  void save_CreateProfile_WhenSuccessful() throws Exception {
    var request = fileUtils.readResourceFile("profile/post-request-profile-rest-assured-200.json");
    var expectedResponse = fileUtils.readResourceFile(
        "profile/post-response-profile-rest-assured-201.json");

    var response = RestAssured
        .given()
        .contentType(ContentType.JSON).accept(ContentType.JSON)
        .log().all()
        .body(request)
        .when()
        .post(V1_PATH_DEFAULT)
        .then()
        .statusCode(HttpStatus.CREATED.value())
        .extract().response().body().asString();

    JsonAssertions.assertThatJson(response)
        .node("id")
        .asNumber()
        .isPositive();

    JsonAssertions.assertThatJson(response)
        .whenIgnoringPaths("id")
        .isEqualTo(expectedResponse);
  }

  @ParameterizedTest
  @MethodSource("postProfileBadRequestSource")
  @DisplayName("save() returns bad request when fields are invalid")
  @Order(4)
  void save_ReturnsBadRequest_WhenFieldAreInvalid(String requestFileName, String responseFileName)
      throws Exception {
    var request = fileUtils.readResourceFile("profile/%s".formatted(requestFileName));
    var responseExpected = fileUtils.readResourceFile("profile/%s".formatted(responseFileName));

    var response = RestAssured
        .given()
        .contentType(ContentType.JSON).accept(ContentType.JSON)
        .log().all()
        .body(request)
        .when()
        .post(V1_PATH_DEFAULT)
        .then()
        .statusCode(HttpStatus.BAD_REQUEST.value())
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

  private static Stream<Arguments> postProfileBadRequestSource() {

    return Stream.of(Arguments.of("post-request-profile-empty-fields-400.json",
            "post-response-profile-empty-fields-400.json"),
        Arguments.of("post-request-profile-blank-fields-400.json",
            "post-response-profile-blank-fields-400.json"));
  }
}