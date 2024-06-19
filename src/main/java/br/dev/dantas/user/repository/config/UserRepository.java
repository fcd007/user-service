package br.dev.dantas.user.repository.config;

import br.dev.dantas.user.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * find email exists in database
     * @param email
     * @return
     */
    Optional<User> findByEmail(String email);
}
