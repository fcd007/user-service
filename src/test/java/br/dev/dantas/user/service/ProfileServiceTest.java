package br.dev.dantas.user.service;

import br.dev.dantas.user.commons.ProfileUtils;
import br.dev.dantas.user.domain.entity.Profile;
import br.dev.dantas.user.repository.config.ProfileRepository;
import exception.NameAllreadyExistsException;
import java.util.List;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ProfileServiceTest {

  @InjectMocks
  private ProfileService service;

  @Mock
  private ProfileRepository repository;

  private List<Profile> profiles;

  @InjectMocks
  private ProfileUtils profileUtils;

  @BeforeEach
  void init() {
    profiles = profileUtils.newProfilesList();
  }

  @Test
  @DisplayName("findAll() returns a list with all profile")
  @Order(1)
  void findAll_ReturnsAllProfiles_WhenSuccessful() {
    BDDMockito.when(repository.findAll()).thenReturn(this.profiles);
    service.findAll(null);
    Assertions.assertThat(profiles).hasSameElementsAs(this.profiles);
  }

  @Test
  @DisplayName("findById() returns a optional profile is id exists")
  @Order(2)
  void findById_ReturnsOptionalProfile_WhenIsIdExists() {
    var id = 1L;
    var animeExpected = this.profiles.get(0);
    BDDMockito.when(repository.findById(id)).thenReturn(Optional.of(animeExpected));

    var profileOptional = service.findById(id);
    Assertions.assertThat(profileOptional).isEqualTo(animeExpected);
  }

  @Test
  @DisplayName("findById() returns a throw ResponseStatusException not found")
  @Order(3)
  void findById_ThrowResponseStatusException_WhenNoProfileIsFound() {
    var id = 1L;
    BDDMockito.when(repository.findById(id)).thenReturn(Optional.empty());

    Assertions.assertThatException()
        .isThrownBy(() -> service.findById(id))
        .isInstanceOf(ResponseStatusException.class);
  }

  @Test
  @DisplayName("save() creates a profile")
  @Order(4)
  void save_CreateProfile_WhenSuccessful() {
    var profileToBeSaved = profileUtils.newProfileToSave();

    BDDMockito.when(repository.save(profileToBeSaved)).thenReturn(profileToBeSaved);
    var profile = service.save(profileToBeSaved);

    Assertions.assertThat(profile).isEqualTo(profileToBeSaved).hasNoNullFieldsOrProperties();
  }

  @Test
  @DisplayName("delete() removes a profile")
  @Order(5)
  void delete_RemovesProfile_WhenSuccessFul() {
    var id = 1L;
    var profileToDelete = this.profiles.get(0);
    BDDMockito.when(repository.findById(id)).thenReturn(Optional.of(profileToDelete));

    Assertions.assertThatNoException().isThrownBy(() -> service.delete(id));
  }

  @Test
  @DisplayName("delete() removes a throw ResponseStatusException not found")
  @Order(6)
  void delete_ThrowResponseStatusException_WhenNoProfileIsFound() {
    var id = 1L;
    BDDMockito.when(repository.findById(id)).thenReturn(Optional.empty());

    Assertions.assertThatException()
        .isThrownBy(() -> service.delete(id))
        .isInstanceOf(ResponseStatusException.class);
  }

  @Test
  @DisplayName("update() updates a profile")
  @Order(7)
  void update_UpdateProfile_WhenSuccessFul() {
    var id = 1L;
    var profileToUpdate = this.profiles.get(0).withName("admin");

    BDDMockito.when(repository.findById(id)).thenReturn(Optional.of(profileToUpdate));
    BDDMockito.when(repository.save(profileToUpdate)).thenReturn(profileToUpdate);

    service.update(profileToUpdate);

    Assertions.assertThatNoException().isThrownBy(() -> service.update(profileToUpdate));
  }

  @Test
  @DisplayName("update() updates a throw ResponseStatusException not found")
  @Order(8)
  void update_ThrowResponseStatusException_WhenNoProfileIsFound() {
    var id = 1L;
    var profileToUpdate = this.profiles.get(0).withName("administrator");
    BDDMockito.when(repository.findById(id)).thenReturn(Optional.empty());

    Assertions.assertThatException()
        .isThrownBy(() -> service.update(profileToUpdate))
        .isInstanceOf(ResponseStatusException.class);
  }

  @Test
  @DisplayName("update() updates NameAllReadyExistsException when name belong to another profile")
  @Order(9)
  void update_ThrowNameAllReadyExistsException_WhenNameBelongAnotherProfile() {
    var id = 1L;
    var profileToUpdate01 = this.profiles.get(0).withName("validator");
    var profileToUpdate02 = this.profiles.get(1).withName(profileToUpdate01.getName());

    BDDMockito.when(repository.findByName(profileToUpdate01.getName()))
        .thenReturn(Optional.of(profileToUpdate02));
    BDDMockito.when(repository.findById(id)).thenReturn(Optional.of(profileToUpdate02));

    Assertions.assertThatException()
        .isThrownBy(() -> service.update(profileToUpdate01))
        .isInstanceOf(NameAllreadyExistsException.class);
  }

  @Test
  @DisplayName("save() updates NameAllReadyExistsException when name belong to another profile")
  @Order(10)
  void save_ThrowNameAllReadyExistsException_WhenNameAllreadyExists() {
    var profileToBeSaved01 = profileUtils.newProfileToSave();
    var profileSave = this.profiles.get(0).withName(profileToBeSaved01.getName());

    BDDMockito.when(repository.findByName(profileToBeSaved01.getName()))
        .thenReturn(Optional.of(profileSave));

    Assertions.assertThatException()
        .isThrownBy(() -> service.save(profileToBeSaved01))
        .isInstanceOf(NameAllreadyExistsException.class);
  }
}