package br.dev.dantas.user.repository.config;

import br.dev.dantas.user.commons.UserUtils;
import br.dev.dantas.user.domain.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;
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

}