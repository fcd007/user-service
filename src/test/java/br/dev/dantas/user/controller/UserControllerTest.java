package br.dev.dantas.user.controller;

import static br.dev.dantas.user.controller.usercontroller.IUserController.V1_PATH_DEFAULT;

import br.dev.dantas.user.commons.FileUtils;
import br.dev.dantas.user.commons.UserUtils;
import br.dev.dantas.user.config.SecurityConfig;
import br.dev.dantas.user.controller.usercontroller.UserController;
import br.dev.dantas.user.domain.mappers.PasswordEncodedMapper;
import br.dev.dantas.user.domain.mappers.UserMapperImpl;
import br.dev.dantas.user.repository.UserData;
import br.dev.dantas.user.repository.UserRepository;
import br.dev.dantas.user.service.UserService;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.server.ResponseStatusException;

@WebMvcTest(UserController.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Import({UserMapperImpl.class, FileUtils.class, UserUtils.class, SecurityConfig.class})
@WithMockUser
class UserControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private UserData userData;

  @Autowired
  private FileUtils fileUtils;

  @MockBean
  private UserRepository repository;

  @MockBean
  private UserService userService;

  @MockBean
  private PasswordEncodedMapper passwordEncodedMapper;

  @Autowired
  private ResourceLoader resourceLoader;

  @Autowired
  private UserUtils userUtils;

  @Test
  @DisplayName("findAll() returns a list with all users")
  @Order(1)
  void findAll_ReturnsAllUsers_WhenSuccessful() throws Exception {
    var response = fileUtils.readResourceFile("user/get-all-three-users-200.json");

    BDDMockito.when(userService.findAll()).thenReturn(userUtils.newUsersList());

    mockMvc.perform(MockMvcRequestBuilders.get(V1_PATH_DEFAULT))
        .andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content().json(response));
  }

  @Test
  @DisplayName("findAll() returns a list with all users")
  @Order(2)
  void findAll_ReturnsAllEmpty_WhenNoUsersAreFound() throws Exception {
    var response = fileUtils.readResourceFile("user/get-all-users-empty-list-200.json");

    BDDMockito.when(userService.findAll()).thenReturn(Collections.emptyList());

    mockMvc.perform(MockMvcRequestBuilders.get(V1_PATH_DEFAULT))
        .andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content().json(response));
  }

  @Test
  @DisplayName("findById() returns empty list when no user is found")
  @Order(3)
  void findById_ReturnsAllUsers_WhenSuccessful() throws Exception {
    var response = fileUtils.readResourceFile(
        "user/get-user-by-id-200.json");
    var id = 1L;

    var userFound = userUtils.newUsersList().stream().filter(user -> user.getId().equals(id))
        .findFirst().orElse(null);
    BDDMockito.when(userService.findById(id)).thenReturn(userFound);

    mockMvc.perform(MockMvcRequestBuilders.get(V1_PATH_DEFAULT + "/{id}", id))
        .andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content().json(response));
  }

  @Test
  @DisplayName("findById() returns a throw ResponseStatusException no user is found")
  @Order(4)
  void findById_ThrowResponseStatusException_WhenNoUserIsFound() throws Exception {
    var id = 10L;
    BDDMockito.when(userService.findById(ArgumentMatchers.any()))
        .thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));

    mockMvc.perform(MockMvcRequestBuilders.get(V1_PATH_DEFAULT + "/{id}", id))
        .andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.status().isNotFound());
  }

  @Test
  @DisplayName("save() create a user")
  @Order(5)
  void save_CreateUser_WhenSuccessful() throws Exception {
    var request = fileUtils.readResourceFile("user/post-request-user-200.json");
    var response = fileUtils.readResourceFile("user/post-response-user-201.json");

    var userToBeSaved = userUtils.newUserToSave();
    BDDMockito.when(userService.save(ArgumentMatchers.any())).thenReturn(userToBeSaved);

    mockMvc.perform(MockMvcRequestBuilders.post(V1_PATH_DEFAULT).content(request)
            .contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print()).andExpect(
            MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isCreated())
        .andExpect(MockMvcResultMatchers.content().json(response));
  }

  @ParameterizedTest
  @MethodSource("postUserBadRequestSource")
  @DisplayName("save() returns bad request when fields are invalid")
  @Order(6)
  void save_ReturnsBadRequest_WhenFieldAreInvalid(String fileName, List<String> errors)
      throws Exception {
    var request = fileUtils.readResourceFile("user/%s".formatted(fileName));

    var mvcResult = mockMvc.perform(
            MockMvcRequestBuilders.post(V1_PATH_DEFAULT).content(request)
                .contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.status().isBadRequest()).andReturn();

    var resolvedException = mvcResult.getResolvedException();
    Assertions.assertThat(mvcResult.getResolvedException()).isNotNull();

    Assertions.assertThat(Objects.requireNonNull(resolvedException).getMessage()).contains(errors);

  }

  @Test
  @DisplayName("delete() removes a user")
  @Order(7)
  void delete_RemovesUser_WhenSuccessFul() throws Exception {
    var id = 1L;
    BDDMockito.doNothing().when(userService).delete(ArgumentMatchers.any());
    mockMvc.perform(MockMvcRequestBuilders.delete(V1_PATH_DEFAULT + "/{id}", id))
        .andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.status().isNoContent());
  }

  @Test
  @DisplayName("delete() removes a throw ResponseStatusException not found to be delete")
  @Order(8)
  void delete_ThrowResponseStatusException_WhenNoUserIsFound() throws Exception {
    var id = 10L;

    BDDMockito.doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND)).when(userService).delete(id);

    mockMvc.perform(MockMvcRequestBuilders.delete(V1_PATH_DEFAULT + "/{id}", id).contentType(MediaType.APPLICATION_JSON))
        .andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.status().isNotFound());
  }

  @Test
  @DisplayName("update() updates an user")
  @Order(9)
  void update_UpdateUser_WhenSuccessFul() throws Exception {
    var request = fileUtils.readResourceFile("user/put-request-user-204.json");

    BDDMockito.doNothing().when(userService).update(ArgumentMatchers.any());

    mockMvc.perform(MockMvcRequestBuilders.put(V1_PATH_DEFAULT).content(request)
            .contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.status().isNoContent());
  }

  @Test
  @DisplayName("update() updates a throw ResponseStatusException not found")
  @Order(10)
  void update_ThrowResponseStatusException_WhenNoUserIsFound() throws Exception {
    var request = fileUtils.readResourceFile("user/put-request-user-404.json");
    var userToUpdated = userUtils.newUserToSave();

    BDDMockito.doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND)).when(userService)
        .update(userToUpdated);

    mockMvc.perform(MockMvcRequestBuilders.put(V1_PATH_DEFAULT).content(request)
            .contentType(MediaType.APPLICATION_JSON))
        .andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.status().isNoContent());
  }

  @ParameterizedTest
  @MethodSource("updateUserBadRequestSource")
  @DisplayName("update() returns bad request when fields are invalid")
  @Order(11)
  void update_ReturnsBadRequest_WhenFieldAreInvalid(String fileName, List<String> errors)
      throws Exception {
    var request = fileUtils.readResourceFile("user/%s".formatted(fileName));

    var mvcResult = mockMvc.perform(
            MockMvcRequestBuilders.put(V1_PATH_DEFAULT).content(request)
                .contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.status().isBadRequest()).andReturn();

    var resolvedException = mvcResult.getResolvedException();
    Assertions.assertThat(mvcResult.getResolvedException()).isNotNull();

    Assertions.assertThat(Objects.requireNonNull(resolvedException).getMessage()).contains(errors);
  }

  private static Stream<Arguments> postUserBadRequestSource() {

    var firstNameError = "the field firstName is required";
    var lastNameError = "the field lastName is required";
    var emailNameError = "the email format is not valid";

    var listErrors = List.of(firstNameError, lastNameError, emailNameError);
    var emailError = Collections.singletonList(emailNameError);

    return Stream.of(Arguments.of("post-request-user-empty-fields-400.json", listErrors),
        Arguments.of("post-request-user-blank-fields-400.json", listErrors),
        Arguments.of("post-request-user-invalid-email-400.json", emailError));
  }

  private static Stream<Arguments> updateUserBadRequestSource() {

    var firstNameError = "the field firstName is required";
    var lastNameError = "the field lastName is required";
    var emailNameError = "the email format is not valid";

    var listErrors = List.of(firstNameError, lastNameError, emailNameError);
    var emailError = Collections.singletonList(emailNameError);

    return Stream.of(Arguments.of(
            "put-request-user-empty-fields-400.json", listErrors),
        Arguments.of("put-request-user-blank-fields-400.json", listErrors),
        Arguments.of("put-request-user-invalid-email-400.json", emailError));
  }
}