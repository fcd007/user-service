package br.dev.dantas.user.service;

import static br.dev.dantas.user.utils.Constants.USER_PROFILE_NOT_FOUND;

import br.dev.dantas.user.domain.entity.User;
import br.dev.dantas.user.domain.entity.UserProfile;
import br.dev.dantas.user.repository.UserProfileRepository;
import exception.NotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor

public class UserProfileService {

  private final UserProfileRepository repository;

  public List<UserProfile> findAll() {
    return repository.findAll();
  }

  public List<User> findAllUsersByProfileId(Long id) {
    return repository.findAllUsersByProfileId(id);
  }


  public UserProfile save(UserProfile userProfile) {
    return repository.save(userProfile);
  }

  public UserProfile findById(Long id) {
    return repository.findById(id).orElseThrow(() -> new NotFoundException(USER_PROFILE_NOT_FOUND));
  }

  public void update(UserProfile userProfileToUpdate) {
    repository.save(userProfileToUpdate);
  }

  public void delete(Long id) {
    var userProfile = findById(id);
    repository.delete(userProfile);
  }
}