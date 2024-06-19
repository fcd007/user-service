package br.dev.dantas.user.service;

import br.dev.dantas.user.domain.entity.User;
import br.dev.dantas.user.repository.config.UserRepository;
import exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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
        assertEmailIsUnique(user.getEmail(), user.getId());
        return repository.save(user);
    }

    public void delete(Long id) {
        var user = findById(id);
        repository.delete(user);
    }

    public void update(User userToUpdate) {

        //validacao para se existe usuario
        assertUserExists(userToUpdate);
        //validacao para verificar se email unico
        assertEmailIsUnique(userToUpdate.getEmail(), userToUpdate.getId());
        repository.save(userToUpdate);
    }

    private void assertUserExists(User userToUpdate) {
        findById(userToUpdate.getId());
    }

    private void assertEmailIsUnique(String email, Long id) {
        repository.findByEmail(email)
                .ifPresent(userNotFound -> {
                    if (!userNotFound.getId().equals(id)) {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email %s already in use ".formatted(email));
                    }
                });
    }
}
