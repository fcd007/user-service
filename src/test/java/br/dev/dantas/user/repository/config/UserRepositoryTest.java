package br.dev.dantas.user.repository.config;

import br.dev.dantas.user.commons.UserUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import({UserUtils.class})
class UserRepositoryTest {

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

}