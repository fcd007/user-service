package br.dev.dantas.user.repository.config;

import br.dev.dantas.user.domain.entity.Profile;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {

  List<Profile> findByName(String name);
}
