package br.dev.dantas.user.controller;

import static br.dev.dantas.user.controller.usercontroller.IUserController.V1_PATH_DEFAULT;

import br.dev.dantas.user.commons.FileUtils;
import br.dev.dantas.user.commons.UserUtils;
import br.dev.dantas.user.configuration.IntegrationTestContainers;
import br.dev.dantas.user.configuration.RestAssuredConfig;
import br.dev.dantas.user.repository.UserRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import java.util.Collections;
import java.util.stream.Stream;
import net.javacrumbs.jsonunit.assertj.JsonAssertions;
import net.javacrumbs.jsonunit.core.Option;
import org.assertj.core.api.Assertions;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    classes = RestAssuredConfig.class)
class UserControllerRestAssuredIT extends IntegrationTestContainers {

  @Autowired
  private UserUtils userUtils;

  @Autowired
  private FileUtils fileUtils;

  @Autowired
  @SpyBean
  private UserRepository userRepository;

  @Autowired
  @Qualifier("requestSpecificationUser")
  private RequestSpecification requestSpecificationUser;

  @Autowired
  @Qualifier("requestSpecificationAdmin")
  private RequestSpecification requestSpecificationAdmin;

  @BeforeEach
  void setUrl() {
    RestAssured.requestSpecification = requestSpecificationUser;
  }

  @Test
  @DisplayName("findAll() returns a list with all users")
  @Order(1)
  @Sql("/sql/user_init_login_admin_test.sql")
  void findAll_ReturnsAllUsers_WhenSuccessful() throws Exception {
    RestAssured.requestSpecification = requestSpecificationAdmin;

    var responseExpected = fileUtils.readResourceFile("user/get-all-one-user-200.json");

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
        });

    JsonAssertions.assertThatJson(response)
        .whenIgnoringPaths("[*].id")
        .isEqualTo(responseExpected);
  }

  @Test
  @DisplayName("findAll() returns an empty list when no users are found")
  @Order(2)
  @Sql("/sql/user_init_login_regular_test.sql")
  void findAll_ReturnsAllEmpty_WhenNoUsersAreFound() throws Exception {
    RestAssured.requestSpecification = requestSpecificationUser;

    var response = fileUtils.readResourceFile("user/get-all-users-empty-list-200.json");

    BDDMockito.when(userRepository.findAll()).thenReturn(Collections.emptyList());

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
  @Order(3)
  @Sql("/sql/user_init_login_admin_test.sql")
  void findById_ReturnsAllUsers_WhenSuccessful() throws Exception {
    RestAssured.requestSpecification = requestSpecificationAdmin;

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
  @DisplayName("findById() returns a throw ResponseStatusException no user is found")
  @Order(4)
  @Sql("/sql/user_init_login_admin_test.sql")
  void findById_ThrowResponseStatusException_WhenNoUserIsFound() throws Exception {
    RestAssured.requestSpecification = requestSpecificationAdmin;

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

  @Test
  @DisplayName("save() create a user")
  @Order(5)
  @Sql("/sql/user_init_login_admin_test.sql")
  void save_CreateUser_WhenSuccessful() throws Exception {
    RestAssured.requestSpecification = requestSpecificationAdmin;

    var request = fileUtils.readResourceFile("user/post-request-user-200.json");
    var responseExpected = fileUtils.readResourceFile("user/post-response-user-201.json");

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
        .isEqualTo(responseExpected);
  }

  @Test
  @DisplayName("delete() removes a user")
  @Order(6)
  @Sql("/sql/user_init_login_admin_test.sql")
  void delete_RemovesUser_WhenSuccessFul() throws Exception {
    RestAssured.requestSpecification = requestSpecificationAdmin;

    var users = userRepository.findAll();
    Assertions.assertThat(users).hasSize(1);

    RestAssured.given().contentType(ContentType.JSON).accept(ContentType.JSON)
        .log().all()
        .when()
        .delete(V1_PATH_DEFAULT + "/{id}", users.get(0).getId())
        .then()
        .statusCode(HttpStatus.NO_CONTENT.value())
        .log().all();
  }

  @Test
  @DisplayName("delete() removes a throw ResponseStatusException not found to be delete")
  @Order(7)
  @Sql("/sql/user_init_login_admin_test.sql")
  void delete_ThrowResponseStatusException_WhenNoUserIsFound() throws Exception {

    RestAssured.requestSpecification = requestSpecificationAdmin;

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

  @Test
  @DisplayName("update() updates an user")
  @Order(8)
  @Sql("/sql/user_init_login_admin_test.sql")
  void update_UpdateUser_WhenSuccessFul() throws Exception {
    RestAssured.requestSpecification = requestSpecificationAdmin;

    var request = fileUtils.readResourceFile("user/put-request-user-204.json");
    var users = userRepository.findAll();
    Assertions.assertThat(users).hasSize(1);

    request.replace("1", users.get(0).getId().toString());

    RestAssured.given().contentType(ContentType.JSON).accept(ContentType.JSON)
        .log().all()
        .body(request)
        .when()
        .put(V1_PATH_DEFAULT)
        .then()
        .log().all()
        .statusCode(HttpStatus.NO_CONTENT.value());
  }

  @Test
  @DisplayName("update() updates a throw ResponseStatusException not found")
  @Order(9)
  @Sql("/sql/user_init_login_admin_test.sql")
  void update_ThrowResponseStatusException_WhenNoUserIsFound() throws Exception {
    RestAssured.requestSpecification = requestSpecificationAdmin;

    var request = fileUtils.readResourceFile("user/put-request-user-404.json");
    var responseExpected = fileUtils.readResourceFile("user/put-request-user-not-found-404.json");

    var response = RestAssured.given().contentType(ContentType.JSON).accept(ContentType.JSON)
        .log().all()
        .body(request)
        .when()
        .put(V1_PATH_DEFAULT)
        .then()
        .log().all()
        .statusCode(HttpStatus.NOT_FOUND.value())
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

  @ParameterizedTest
  @MethodSource("postUserBadRequestSource")
  @DisplayName("save() returns bad request when fields are invalid")
  @Order(10)
  @Sql("/sql/user_init_login_admin_test.sql")
  void save_ReturnsBadRequest_WhenFieldAreInvalid(String requestFileName, String responseFileName)
      throws Exception {

    RestAssured.requestSpecification = requestSpecificationAdmin;

    var request = fileUtils.readResourceFile("user/%s".formatted(requestFileName));
    var responseExpected = fileUtils.readResourceFile("user/%s".formatted(responseFileName));

    var response = RestAssured
        .given()
        .contentType(ContentType.JSON).accept(ContentType.JSON)
        .log().all()
        .body(request)
        .when()
        .post(V1_PATH_DEFAULT)
        .then()
        .statusCode(HttpStatus.BAD_REQUEST.value())
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

  private static Stream<Arguments> postUserBadRequestSource() {
    return Stream.of(Arguments.of("post-request-user-empty-fields-400.json", "post-response-user-empty-fields-400.json"),
        Arguments.of("post-request-user-blank-fields-400.json", "post-response-user-blank-fields-400.json"),
        Arguments.of("post-request-user-invalid-email-400.json", "post-response-user-invalid-email-400.json"));
  }

  @ParameterizedTest
  @MethodSource("updateUserBadRequestSource")
  @DisplayName("update() returns bad request when fields are invalid")
  @Order(11)
  @Sql("/sql/user_init_login_admin_test.sql")
  void update_ReturnsBadRequest_WhenFieldAreInvalid(String requestFileName, String responseFileName)
      throws Exception {

    RestAssured.requestSpecification = requestSpecificationAdmin;

    var request = fileUtils.readResourceFile("user/%s".formatted(requestFileName));
    var responseExpected = fileUtils.readResourceFile("user/%s".formatted(responseFileName));

    var response = RestAssured
        .given()
        .contentType(ContentType.JSON).accept(ContentType.JSON)
        .log().all()
        .body(request)
        .when()
        .put(V1_PATH_DEFAULT)
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

  private static Stream<Arguments> updateUserBadRequestSource() {
    return Stream.of(Arguments.of("put-request-user-empty-fields-400.json", "put-response-user-empty-fields-400.json"),
        Arguments.of("put-request-user-blank-fields-400.json", "put-response-user-blank-fields-400.json"),
        Arguments.of("put-request-user-invalid-email-400.json", "put-response-user-invalid-email-400.json"));
  }
}