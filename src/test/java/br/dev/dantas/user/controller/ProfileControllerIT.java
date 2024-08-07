package br.dev.dantas.user.controller;

import static br.dev.dantas.user.controller.profilecontroller.IProfileController.V1_PATH_DEFAULT;

import br.dev.dantas.user.commons.ProfileUtils;
import br.dev.dantas.user.configuration.IntegrationTestContainers;
import br.dev.dantas.user.controller.profilecontroller.response.ProfilePostResponse;
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
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProfileControllerIT extends IntegrationTestContainers {

  @Autowired
  private TestRestTemplate testRestTemplate;

  @Autowired
  private ProfileUtils profileUtils;


  @Test
  @DisplayName("findAll() returns a list with all profiles when successful")
  @Order(1)
  @Sql("/sql/init_two_profiles.sql")
  void findAll_ReturnsAllUsers_WhenSuccessful() {
    var typeReference = new ParameterizedTypeReference<List<Profile>>() {};
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

  @Test
  @DisplayName("findAll() returns a list empty when no profiles are found")
  @Order(2)
  @Sql("/sql/clean_two_profiles.sql")
  void findAll_ReturnsAllEmpty_WhenNoUsersAreFound() {
    var typeReference = new ParameterizedTypeReference<List<Profile>>() {
    };
    var response = testRestTemplate.exchange(V1_PATH_DEFAULT, HttpMethod.GET, null, typeReference);

    Assertions.assertThat(response).isNotNull();
    Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    Assertions.assertThat(response.getBody()).isNotNull().isEmpty();
  }

  @Test
  @DisplayName("save() create a profile when successful")
  @Order(3)
  void save_CreateProfile_WhenSuccessful() {
    var profileToSave = profileUtils.newProfileToSave();

    var response = testRestTemplate.exchange(V1_PATH_DEFAULT, HttpMethod.POST, new HttpEntity<>(profileToSave), ProfilePostResponse.class);

    Assertions.assertThat(response).isNotNull();
    Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    Assertions.assertThat(response.getBody()).isNotNull().hasNoNullFieldsOrProperties();
  }
}