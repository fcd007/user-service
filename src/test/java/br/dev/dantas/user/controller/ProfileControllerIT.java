package br.dev.dantas.user.controller;

import static br.dev.dantas.user.controller.profilecontroller.IProfileController.V1_PATH_DEFAULT;

import br.dev.dantas.user.domain.entity.Profile;
import java.util.List;
import java.util.Objects;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProfileControllerIT {

  @Autowired
  private TestRestTemplate testRestTemplate;


  @Test
  @DisplayName("findAll() returns a list with all profiles")
  @Order(1)
  @Sql("/sql/init_two_profiles.sql")
  void findAll_ReturnsAllUsers_WhenSuccessful() {
    var typeReference = new ParameterizedTypeReference<List<Profile>>(){};
    var response = testRestTemplate.exchange(V1_PATH_DEFAULT, HttpMethod.GET, null, typeReference);

    Assertions.assertThat(response).isNotNull();
    Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    Assertions.assertThat(response.getBody()).isNotNull().doesNotContainNull();

    Objects.requireNonNull(response.getBody()).forEach(profile -> {
      Assertions.assertThat(profile.getId()).isNotNull();
      Assertions.assertThat(profile.getName()).isNotNull();
      Assertions.assertThat(profile.getDescription()).isNotNull();
    });
  }
}