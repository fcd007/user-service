package br.dev.dantas.user.repository.config;

import br.dev.dantas.user.domain.entity.Profile;
import br.dev.dantas.user.domain.entity.User;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class ProfileData {
    private List<Profile> profiles = new ArrayList<>();

    {
        var admin = Profile.builder().id(1L).name("admin").description("profile administrator").createdAt(LocalDateTime.now()).lastUpdatedOn(LocalDateTime.now()).build();
        var develop = Profile.builder().id(2L).name("admin").description("profile develop").createdAt(LocalDateTime.now()).lastUpdatedOn(LocalDateTime.now()).build();
        var test = Profile.builder().id(3L).name("admin").description("profile test").createdAt(LocalDateTime.now()).lastUpdatedOn(LocalDateTime.now()).build();

        profiles.addAll(List.of(admin, develop, test));
    }

    public List<Profile> getUsers() {
        return profiles;
    }
}
