package br.dev.dantas.user.repository.config;

import br.dev.dantas.user.domain.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserHardCodeRepository {

    private final UserData userData;

    public List<User> findAll() {
        return userData.getUsers();
    }

    public Optional<User> findById(Long id) {
        return userData.getUsers().stream().filter(user -> user.getId().equals(id)).findFirst();
    }

    public List<User> findByName(String name) {
        return name == null ? userData.getUsers() : userData.getUsers().stream().filter(user -> user.getFirstName().equalsIgnoreCase(name)).toList();
    }

    public User save(User user) {
        userData.getUsers().add(user);
        return user;
    }

    public void delete(User user) {
        userData.getUsers().remove(user);
    }

    public void update(User userToUpdate) {
        delete(userToUpdate);
        save(userToUpdate);
    }
}
