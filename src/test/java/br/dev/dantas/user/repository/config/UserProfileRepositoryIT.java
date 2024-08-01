package br.dev.dantas.user.repository.config;

import br.dev.dantas.user.configuration.IntegrationTestContainers;
import br.dev.dantas.user.repository.UserProfileRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.junit.jupiter.Testcontainers;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Testcontainers
class UserProfileRepositoryIT extends IntegrationTestContainers {

  @Autowired
  private UserProfileRepository userProfileRepository;

  @Test
  @DisplayName("findAll() returns a list with all users by profile id")
  @Order(1)
  @Sql("/sql/userprofile_init_test.sql")
  void findAllUsersByProfileId_ReturnsAllUsers_WhenSuccessful() {
    var profileId = 999L;
    var users = userProfileRepository.findAllUsersByProfileId(profileId);

    Assertions.assertThat(users).isNotEmpty().hasSize(2);

    users.forEach(user -> Assertions.assertThat(user).hasNoNullFieldsOrProperties());
  }
}