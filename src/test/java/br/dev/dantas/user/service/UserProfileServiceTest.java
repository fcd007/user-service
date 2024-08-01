package br.dev.dantas.user.service;

import br.dev.dantas.user.commons.ProfileUtils;
import br.dev.dantas.user.commons.UserProfileUtils;
import br.dev.dantas.user.commons.UserUtils;
import br.dev.dantas.user.domain.entity.UserProfile;
import br.dev.dantas.user.repository.UserProfileRepository;
import java.util.List;
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
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserProfileServiceTest {

  @InjectMocks
  private UserProfileService service;

  @Mock
  private UserProfileRepository repository;

  private List<UserProfile> userProfiles;

  @InjectMocks
  private UserProfileUtils userProfileUtils;

  @Spy
  private UserUtils userUtils;

  @Spy
  private ProfileUtils profileUtils;

  @BeforeEach
  void init() {
    userProfiles = userProfileUtils.newUserProfilesList();
  }

  @Test
  @DisplayName("findAll() returns a list with all user profile")
  @Order(1)
  void findAll_ReturnsAllProfiles_WhenSuccessful() {
    BDDMockito.when(repository.findAll()).thenReturn(this.userProfiles);
    var profiles = service.findAll();
    Assertions.assertThat(profiles).hasSameElementsAs(this.userProfiles);
  }

  @Test
  @DisplayName("findAllUsersByProfileId returns a list for users for a  given profiles")
  @Order(2)
  void findAllUsersByProfileId_ReturnsOptionalAllUsersProfile_WhenIsIdExists() {
    var profileId = 99L;
    var usersProfilesList = userProfiles
        .stream()
        .filter(userprofile -> userprofile.getProfile().getId().equals(profileId))
        .map(UserProfile::getUser)
        .toList();
    BDDMockito.when(repository.findAllUsersByProfileId(profileId)).thenReturn(usersProfilesList);

    var users = service.findAllUsersByProfileId(profileId);
    Assertions.assertThat(users).hasSize(1).hasSameElementsAs(usersProfilesList);
  }
}