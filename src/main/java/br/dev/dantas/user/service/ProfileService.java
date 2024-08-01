package br.dev.dantas.user.service;

import static br.dev.dantas.user.utils.Constants.*;

import br.dev.dantas.user.domain.entity.Profile;
import br.dev.dantas.user.repository.ProfileRepository;
import exception.*;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class ProfileService {

  private final ProfileRepository repository;


  public List<Profile> findAll(String name) {
    return name != null ? Collections.singletonList((findByName(name))) : repository.findAll();
  }

  public Profile findByName(String name) {
    var profile = repository.findByName(name);
    return profile.orElse(null);
  }

  public Profile save(Profile profile) {
    assertNameIsUnique(profile.getName(), profile.getId());
    return repository.save(profile);
  }

  public Profile findById(Long id) {
    return repository.findById(id)
        .orElseThrow(() -> new NotFoundException(PROFILE_NOT_FOUND));
  }

  public void delete(Long id) {
    var profile = findById(id);
    repository.delete(profile);
  }

  public void update(Profile profileToUpdate) {
    //validacao para se existe usuario
    assertProfileExists(profileToUpdate);
    //validacao para verificar se email unico
    assertNameIsUnique(profileToUpdate.getName(), profileToUpdate.getId());
    repository.save(profileToUpdate);
  }

  private void assertProfileExists(Profile profileToUpdate) {
    findById(profileToUpdate.getId());
  }

  private void assertNameIsUnique(String name, Long id) {
    repository.findByName(name)
        .ifPresent(userNotFound -> {
          if (!userNotFound.getId().equals(id)) {
            throw new NameAllreadyExistsException("Email %s already in use ".formatted(name));
          }
        });
  }
}