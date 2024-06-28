package br.dev.dantas.user.repository.config;

import br.dev.dantas.user.domain.entity.UserProfile;
import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {

  @Query("SELECT up FROM UserProfile up join fetch up.user u join fetch up.profile p")
  List<UserProfile> retrieveAll();

  @EntityGraph(attributePaths = {"user", "profile"})
  List<UserProfile> findAll();
}
