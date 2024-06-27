package br.dev.dantas.user.service;

import static br.dev.dantas.user.utils.Constants.PROFILE_NOT_FOUND;

import br.dev.dantas.user.domain.entity.Profile;
import br.dev.dantas.user.repository.config.ProfileRepository;
import exception.NotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor

public class ProfileService {

  private final ProfileRepository repository;


  public List<Profile> findAll(String name) {
    return findByName(name);
  }

  public List<Profile> findByName(String name) {
    return name != null ? repository.findByName(name) : repository.findAll();
  }

  public Profile save(Profile profile) {
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

  public void update(Profile ProfileToUpdate) {
    var profile = findById(ProfileToUpdate.getId());
    repository.save(profile);
  }
}