package br.dev.dantas.user.service;

import br.dev.dantas.user.domain.entity.User;
import br.dev.dantas.user.repository.UserRepository;
import br.dev.dantas.user.utils.Constants;
import exception.EmailAllreadyExistsException;
import exception.NotFoundException;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository repository;

  public List<User> findAll() {
    return repository.findAll();
  }

  public User findById(Long id) {
    return repository.findById(id)
        .orElseThrow(() -> new NotFoundException(Constants.USER_NOT_FOUND));
  }

  /**
   * méetodo cria um novo usuario, usar save com transacao na operacao ela sera ativada para caso de
   * exceções do tipo RuntimeException
   *
   * @param user
   * @return new user
   */
  @Transactional
  public User save(User user) {
    assertEmailIsUnique(user.getEmail(), user.getId());
    return repository.save(user);
  }

  public void delete(Long id) {
    var user = findById(id);
    repository.delete(user);
  }

  public void update(User userToUpdatepatial) {
    //validacao para se existe usuario
    var userSaved = findById(userToUpdatepatial.getId());

    //validacao para verificar se email unico
    assertEmailIsUnique(userToUpdatepatial.getEmail(), userToUpdatepatial.getId());

    //verificar se user esta trocando senha e roles
    var password = userToUpdatepatial.getPassword() == null ? userSaved.getPassword() : userToUpdatepatial.getPassword();
    var roles = userToUpdatepatial.getRoles() == null ? userSaved.getRoles() : userToUpdatepatial.getRoles();

    //criando objeto imutavel
    var userToUpdate = userToUpdatepatial.withPassword(password).withRoles(roles);

    repository.save(userToUpdate);
  }

  private void assertUserExists(User userToUpdate) {
    findById(userToUpdate.getId());
  }

  private void assertEmailIsUnique(String email, Long id) {
    repository.findByEmail(email).ifPresent(userNotFound -> {
      if (!userNotFound.getId().equals(id)) {
        throw new EmailAllreadyExistsException("Email %s already in use ".formatted(email));
      }
    });
  }
}
