package br.dev.dantas.user.repository;

import br.dev.dantas.user.domain.entity.Profile;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import org.springframework.stereotype.Component;

@Getter
@Component
public class ProfileData {
    private List<Profile> profiles = new ArrayList<>();

    {
        var admin = Profile.builder().id(1L).name("admin").description("profile administrator").createdAt(LocalDateTime.now()).lastUpdatedOn(LocalDateTime.now()).build();
        var develop = Profile.builder().id(2L).name("develop").description("profile develop").createdAt(LocalDateTime.now()).lastUpdatedOn(LocalDateTime.now()).build();
        var test = Profile.builder().id(3L).name("test").description("profile test").createdAt(LocalDateTime.now()).lastUpdatedOn(LocalDateTime.now()).build();

        profiles.addAll(List.of(admin, develop, test));
    }

}
