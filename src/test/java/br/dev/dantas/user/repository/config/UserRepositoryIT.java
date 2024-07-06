package br.dev.dantas.user.repository.config;

import br.dev.dantas.user.commons.UserUtils;
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
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.junit.jupiter.Testcontainers;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Import({UserUtils.class})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Testcontainers
class UserRepositoryIT extends IntegrationTestContainers {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private UserUtils userUtils;

  @Test
  void save() {
    var userToSave = userUtils.newUserToSave();
    var user = userRepository.save(userToSave);
    Assertions.assertThat(user).hasNoNullFieldsOrProperties();
  }

  @Test
  @DisplayName("findAll() returns a list with all users")
  @Order(1)
  @Sql("/sql/user_init_test.sql")
  void findAll_ReturnsAllUsers_WhenSuccessful() {
    var users = userRepository.findAll();
    Assertions.assertThat(users).isNotEmpty();
  }
}