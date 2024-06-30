package br.dev.dantas.user.commons;

import br.dev.dantas.user.domain.entity.UserProfile;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserProfileUtils {

  private final UserUtils userUtils;
  private final ProfileUtils profileUtils;

  public List<UserProfile> newUserProfilesList() {
    var admin = newUserProfileToSave();
    return new ArrayList<>(List.of(admin));
  }

  public UserProfile newUserProfileToSave() {
    return UserProfile.builder()
        .id(1L)
        .user(userUtils.newUserSaved())
        .profile(profileUtils.newProfileSaved())
        .build();
  }

  public UserProfile newUserProfileSaved() {
    return UserProfile.builder()
        .id(1L)
        .user(userUtils.newUserSaved())
        .profile(profileUtils.newProfileSaved())
        .build();
  }
}
