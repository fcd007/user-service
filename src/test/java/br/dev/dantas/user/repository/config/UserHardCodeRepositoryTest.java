package br.dev.dantas.user.repository.config;

import br.dev.dantas.user.commons.UserUtils;
import br.dev.dantas.user.domain.entity.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;


@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserHardCodeRepositoryTest {

    @InjectMocks
    private UserHardCodeRepository repository;

    @Mock
    private UserData userData;

    private List<User> users;

    @InjectMocks
    private UserUtils userUtils;

    @BeforeEach
    void init() {

        users = userUtils.newUsersList();

        BDDMockito.when(userData.getUsers()).thenReturn(users);
    }

    @Test
    @DisplayName("findAll() returns a list with all users")
    @Order(1)
    void findAll_ReturnsAllUsers_WhenSuccessful() {
        var users = repository.findAll();
        Assertions.assertThat(userData.getUsers()).hasSameElementsAs(users);
    }

    @Test
    @DisplayName("findById() returns an object with given id")
    @Order(2)
    void findById_ReturnsAllUsers_WhenSuccessful() {
        var userOptional = repository.findById(3L);
        Assertions.assertThat(userOptional).isPresent().contains(users.get(2));
    }
    @Test
    @DisplayName("save() creates a user")
    @Order(3)
    void save_CreatesUser_WhenSuccessFul() {
        var userToBeSaved = userUtils.newUserToSave();

        var user = repository.save(userToBeSaved);

        Assertions.assertThat(user).isEqualTo(userToBeSaved).hasNoNullFieldsOrProperties();

        var userSaved = repository.findAll();
        Assertions.assertThat(userSaved).contains(userToBeSaved);
    }

    @Test
    @DisplayName("delete() removes a user")
    @Order(4)
    void delete_RemovesAnime_WhenSuccessFul() {
        var userToDelete = this.users.get(0);

        repository.delete(userToDelete);

        Assertions.assertThat(this.users).doesNotContain(userToDelete);
    }

}