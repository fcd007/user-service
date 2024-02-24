package br.dev.dantas.user.service;

import br.dev.dantas.user.commons.UserUtils;
import br.dev.dantas.user.domain.entity.User;
import br.dev.dantas.user.repository.config.UserHardCodeRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.assertj.core.api.Assertions;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserServiceTest {

    @InjectMocks
    private UserService service;

    @Mock
    private UserHardCodeRepository repository;

    private List<User> users;

    @InjectMocks
    private UserUtils userUtils;

    @BeforeEach
    void init() {
        users = userUtils.newUsersList();
    }

    @Test
    @DisplayName("findAll() returns a list with all users")
    @Order(1)
    void findAll_ReturnsAllProducers_WhenSuccessful() {
        BDDMockito.when(repository.findAll()).thenReturn(this.users);

        var users = service.findAll();
        Assertions.assertThat(users).hasSameElementsAs(this.users);
    }

    @Test
    @DisplayName("findById() returns a optional user is id exists")
    @Order(2)
    void findById_ReturnsOptionalUser_WhenIsIdExists() {
        var id = 1L;
        var animeExpected = this.users.get(0);
        BDDMockito.when(repository.findById(id)).thenReturn(Optional.of(animeExpected));

        var userOptional = service.findById(id);
        Assertions.assertThat(userOptional).isEqualTo(animeExpected);
    }

    @Test
    @DisplayName("findById() returns a throw ResponseStatusException not found")
    @Order(3)
    void findById_ThrowResponseStatusException_WhenNoUserIsFound() {
        var id = 1L;
        BDDMockito.when(repository.findById(id)).thenReturn(Optional.empty());

        Assertions.assertThatException()
                .isThrownBy(() -> service.findById(id))
                .isInstanceOf(ResponseStatusException.class);
    }

}