package br.dev.dantas.user.repository.config;

import br.dev.dantas.user.commons.UserUtils;
import static br.dev.dantas.user.configuration.IntegrationTestContainers.*;

import br.dev.dantas.user.configuration.IntegrationTestContainers;
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
import org.springframework.context.annotation.Import;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.junit.jupiter.Testcontainers;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Import({UserUtils.class})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Testcontainers
class UserProfileRepositoryTest implements IntegrationTestContainers {

  @Autowired
  private UserProfileRepository userProfileRepository;

  @Autowired
  private UserUtils userUtils;


  @Test
  @DisplayName("findAll() returns a list with all users by profile id")
  @Order(1)
  @Sql("/sql/userprofile_init_test.sql")
  void findAllUsersByProfileId_ReturnsAllUsers_WhenSuccessful() {
    var profileId = 1L;
    var users = userProfileRepository.findAllUsersByProfileId(profileId);

    Assertions.assertThat(users).isNotEmpty().hasSize(2);

    users.forEach(user -> Assertions.assertThat(user).hasNoNullFieldsOrProperties());
  }

  @DynamicPropertySource
  static void mysqProperties(DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.url", mysqlContainer::getJdbcUrl);
    registry.add("spring.datasource.username", mysqlContainer::getUsername);
    registry.add("spring.datasource.password", mysqlContainer::getPassword);
  }
}