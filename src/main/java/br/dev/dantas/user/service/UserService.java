package br.dev.dantas.user.service;

import br.dev.dantas.user.domain.entity.User;
import br.dev.dantas.user.repository.config.UserRepository;
import exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;

    public List<User> findAll() {
        return repository.findAll();
    }

    public User findById(Long id) {
        return repository.findById(id).orElseThrow(() -> new NotFoundException("User not found"));
    }

    public User save(User user) {
        return repository.save(user);
    }

    public void delete(Long id) {
        var user = findById(id);
        repository.delete(user);
    }

    public void update(User userToUpdate) {
        assertUserExists(userToUpdate);

        repository.save(userToUpdate);
    }

    private void assertUserExists(User userToUpdate) {
        findById(userToUpdate.getId());
    }
}
