package br.dev.dantas.user.commons;

import br.dev.dantas.user.domain.entity.Profile;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class ProfileUtils {

  public List<Profile> newProfilesList() {
    var admin = Profile.builder()
        .id(1L).name("administrator")
        .description("Profile user administrator")
        .createdAt(LocalDateTime.now())
        .lastUpdatedOn(LocalDateTime.now())
        .build();
    var guest = Profile.builder()
        .id(2L).name("guest")
        .description("Profile user guest")
        .createdAt(LocalDateTime.now())
        .lastUpdatedOn(LocalDateTime.now())
        .build();
    var dev = Profile.builder()
        .id(3L).name("develop")
        .description("Profile user develop")
        .createdAt(LocalDateTime.now())
        .lastUpdatedOn(LocalDateTime.now())
        .build();

    return new ArrayList<>(List.of(admin, guest, dev));
  }

  public Profile newProfileToSave() {
    return Profile.builder()
        .id(99L).name("Test")
        .description("Profile user test")
        .createdAt(LocalDateTime.now())
        .lastUpdatedOn(LocalDateTime.now())
        .build();
  }

  public Profile newProfileSaved() {
    return Profile.builder()
        .id(99L).name("Test")
        .description("Profile user test")
        .createdAt(LocalDateTime.now())
        .lastUpdatedOn(LocalDateTime.now())
        .build();
  }
}
