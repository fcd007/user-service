package br.dev.dantas.user.controller;

import static br.dev.dantas.user.controller.profilecontroller.IProfileController.V1_PATH_DEFAULT;

import br.dev.dantas.user.commons.FileUtils;
import br.dev.dantas.user.commons.ProfileUtils;
import br.dev.dantas.user.controller.profilecontroller.ProfileController;
import br.dev.dantas.user.domain.mappers.ProfileMapperImpl;
import br.dev.dantas.user.repository.config.ProfileData;
import br.dev.dantas.user.repository.config.ProfileRepository;
import br.dev.dantas.user.service.ProfileService;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.server.ResponseStatusException;

@WebMvcTest(ProfileController.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Import({ProfileMapperImpl.class, FileUtils.class, ProfileUtils.class})
class ProfileControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private ProfileData profileData;

  @Autowired
  private FileUtils fileUtils;

  @MockBean
  private ProfileRepository repository;

  @MockBean
  private ProfileService profileService;

  @Autowired
  private ResourceLoader resourceLoader;

  @Autowired
  private ProfileUtils profileUtils;

  @Test
  @DisplayName("findAll() returns a list with all profiles")
  @Order(1)
  void findAll_ReturnsAllUsers_WhenSuccessful() throws Exception {
    var response = fileUtils.readResourceFile("profile/get-all-profile-200.json");

    BDDMockito.when(profileService.findAll(null)).thenReturn(profileUtils.newProfilesList());

    mockMvc.perform(MockMvcRequestBuilders.get(V1_PATH_DEFAULT))
        .andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content().json(response));
  }

  @Test
  @DisplayName("findAll() returns a list with all profiles")
  @Order(2)
  void findAll_ReturnsAllEmpty_WhenNoUsersAreFound() throws Exception {
    var response = fileUtils.readResourceFile("profile/get-all-profiles-empty-list-200.json");

    BDDMockito.when(profileService.findAll(null)).thenReturn(Collections.emptyList());

    mockMvc.perform(MockMvcRequestBuilders.get(V1_PATH_DEFAULT))
        .andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content().json(response));
  }

  @Test
  @DisplayName("findById() returns empty list when no profile is found")
  @Order(3)
  void findById_ReturnsAllUsers_WhenSuccessful() throws Exception {
    var response = fileUtils.readResourceFile("profile/get-profile-by-id-200.json");
    var id = 1L;

    var profileFound = profileUtils.newProfilesList().stream().filter(profile -> profile.getId().equals(id)).findFirst().orElse(null);
    BDDMockito.when(profileService.findById(id)).thenReturn(profileFound);

    mockMvc.perform(MockMvcRequestBuilders.get(V1_PATH_DEFAULT + "/{id}", id))
        .andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content().json(response));
  }

  @Test
  @DisplayName("findById() returns a throw ResponseStatusException no profile is found")
  @Order(4)
  void findById_ThrowResponseStatusException_WhenNoProfileIsFound() throws Exception {
    var id = 10L;
    BDDMockito.when(profileService.findById(ArgumentMatchers.any()))
        .thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));

    mockMvc.perform(MockMvcRequestBuilders.get(V1_PATH_DEFAULT + "/{id}", id))
        .andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.status().isNotFound());
  }

  @Test
  @DisplayName("save() create a profile")
  @Order(5)
  void save_CreateProfile_WhenSuccessful() throws Exception {
    var request = fileUtils.readResourceFile("profile/post-request-profile-200.json");
    var response = fileUtils.readResourceFile("profile/post-response-profile-201.json");

    var profileToBeSaved = profileUtils.newProfileToSave();
    BDDMockito.when(profileService.save(ArgumentMatchers.any())).thenReturn(profileToBeSaved);

    mockMvc.perform(MockMvcRequestBuilders.post(V1_PATH_DEFAULT).content(request)
            .contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print()).andExpect(
            MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isCreated())
        .andExpect(MockMvcResultMatchers.content().json(response));
  }

  @ParameterizedTest
  @MethodSource("postProfileBadRequestSource")
  @DisplayName("save() returns bad request when fields are invalid")
  @Order(6)
  void save_ReturnsBadRequest_WhenFieldAreInvalid(String fileName, List<String> errors)
      throws Exception {
    var request = fileUtils.readResourceFile("profile/%s".formatted(fileName));

    var mvcResult = mockMvc.perform(
            MockMvcRequestBuilders.post(V1_PATH_DEFAULT).content(request)
                .contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.status().isBadRequest()).andReturn();

    var resolvedException = mvcResult.getResolvedException();
    Assertions.assertThat(mvcResult.getResolvedException()).isNotNull();

    Assertions.assertThat(Objects.requireNonNull(resolvedException).getMessage()).contains(errors);

  }

  @Test
  @DisplayName("delete() removes a profile")
  @Order(7)
  void delete_RemovesProfile_WhenSuccessFul() throws Exception {
    var id = 1L;
    BDDMockito.doNothing().when(profileService).delete(ArgumentMatchers.any());
    mockMvc.perform(MockMvcRequestBuilders.delete(V1_PATH_DEFAULT + "/{id}", id))
        .andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.status().isNoContent());
  }

  @Test
  @DisplayName("delete() removes a throw ResponseStatusException not found to be delete")
  @Order(8)
  void delete_ThrowResponseStatusException_WhenNoProfileIsFound() throws Exception {
    var id = 10L;
    BDDMockito.doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND)).when(profileService)
        .delete(id);

    mockMvc.perform(MockMvcRequestBuilders.delete(V1_PATH_DEFAULT + "/{id}", id))
        .andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.status().isNotFound());
  }

  @Test
  @DisplayName("update() updates a profile")
  @Order(9)
  void update_UpdateProfile_WhenSuccessFul() throws Exception {
    var request = fileUtils.readResourceFile("profile/put-request-profile-204.json");

    BDDMockito.doNothing().when(profileService).update(ArgumentMatchers.any());

    mockMvc.perform(MockMvcRequestBuilders.put(V1_PATH_DEFAULT).content(request)
            .contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.status().isNoContent());
  }

  @Test
  @DisplayName("update() updates a throw ResponseStatusException not found")
  @Order(10)
  void update_ThrowResponseStatusException_WhenNoProfileIsFound() throws Exception {
    var request = fileUtils.readResourceFile("profile/put-request-profile-404.json");
    var profileToUpdated = profileUtils.newProfileToSave();

    BDDMockito.doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND)).when(profileService)
        .update(profileToUpdated);

    mockMvc.perform(MockMvcRequestBuilders.put(V1_PATH_DEFAULT).content(request)
            .contentType(MediaType.APPLICATION_JSON))
        .andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.status().isNoContent());
  }

  @ParameterizedTest
  @MethodSource("updateProfileBadRequestSource")
  @DisplayName("update() returns bad request when fields are invalid")
  @Order(11)
  void update_ReturnsBadRequest_WhenFieldAreInvalid(String fileName, List<String> errors) throws Exception {
    var request = fileUtils.readResourceFile("profile/%s".formatted(fileName));

    var mvcResult = mockMvc.perform(
            MockMvcRequestBuilders.put(V1_PATH_DEFAULT).content(request)
                .contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.status().isBadRequest()).andReturn();

    var resolvedException = mvcResult.getResolvedException();
    Assertions.assertThat(mvcResult.getResolvedException()).isNotNull();

    Assertions.assertThat(Objects.requireNonNull(resolvedException).getMessage()).contains(errors);
  }

  private static Stream<Arguments> postProfileBadRequestSource() {

    var NameError = "the field name is required";
    var descriptionError = "the field description is required";

    var listErrors = List.of(NameError, descriptionError);

    return Stream.of(Arguments.of("post-request-profile-empty-fields-400.json", listErrors),
        Arguments.of("post-request-profile-blank-fields-400.json", listErrors));
  }

  private static Stream<Arguments> updateProfileBadRequestSource() {

    var nameError = "the field name is required";
    var descriptionError = "the field description is required";

    var listErrors = List.of(nameError, descriptionError);

    return Stream.of(Arguments.of("put-request-profile-empty-fields-400.json", listErrors),
        Arguments.of("put-request-profile-blank-fields-400.json", listErrors));
  }
}