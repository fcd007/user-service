package br.dev.dantas.user.controller;

import br.dev.dantas.user.commons.FileUtils;
import br.dev.dantas.user.commons.UserUtils;
import br.dev.dantas.user.repository.config.UserHardCodeRepository;
import br.dev.dantas.user.service.UserService;
import org.junit.jupiter.api.*;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;

@WebMvcTest(UserController.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserControllerTest {

    @MockBean
    private UserService userService;

    @SpyBean
    private UserHardCodeRepository repository;
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ResourceLoader resourceLoader;

    @Autowired
    private FileUtils fileUtils;

    @Autowired
    private UserUtils userUtils;

    @Test
    @DisplayName("findAll() returns a list with all users")
    @Order(1)
    void findAll_ReturnsAllUsers_WhenSuccessful() throws Exception {
        var response = fileUtils.readResourceFile("user/get-all-users-200.json");

        BDDMockito.when(userService.findAll()).thenReturn(userUtils.newUsersList());

        mockMvc.perform(MockMvcRequestBuilders.get(IUserController.V1_PATH_DEFAULT))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("findAll() returns a list with all users")
    @Order(1)
    void findAll_ReturnsAllEmpty_WhenNoUsersAreFound() throws Exception {
        var response = fileUtils.readResourceFile("user/get-all-users-empty-list-200.json");

        BDDMockito.when(userService.findAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(MockMvcRequestBuilders.get(IUserController.V1_PATH_DEFAULT))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("findById() returns empty list when no user is found")
    @Order(2)
    void findById_ReturnsAllUsers_WhenSuccessful() throws Exception {
        var response = fileUtils.readResourceFile("user/get-user-by-id-200.json");
        var id = 1L;

        var userFound = userUtils.newUsersList()
                .stream()
                .filter(user -> user.getId().equals(id))
                .findFirst()
                .orElse(null);
        BDDMockito.when(userService.findById(id)).thenReturn(userFound);

        mockMvc.perform(MockMvcRequestBuilders.get(IUserController.V1_PATH_DEFAULT + "/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("findById() returns a throw ResponseStatusException no user is found")
    @Order(3)
    void findById_ThrowResponseStatusException_WhenNoUserIsFound() throws Exception {
        var id = 10L;
        BDDMockito.when(userService.findById(ArgumentMatchers.any())).thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));

        mockMvc.perform(MockMvcRequestBuilders.get(IUserController.V1_PATH_DEFAULT + "/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}