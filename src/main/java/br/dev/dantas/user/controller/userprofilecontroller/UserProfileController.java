package br.dev.dantas.user.controller.userprofilecontroller;

import br.dev.dantas.user.controller.usercontroller.IUserController;
import br.dev.dantas.user.controller.userprofilecontroller.request.UserProfilePostRequest;
import br.dev.dantas.user.controller.userprofilecontroller.request.UserProfilePutRequest;
import br.dev.dantas.user.controller.userprofilecontroller.response.UserProfileGetResponse;
import br.dev.dantas.user.controller.userprofilecontroller.response.UserProfilePostResponse;
import br.dev.dantas.user.controller.userprofilecontroller.response.UserProfileUsersGetResponse;
import br.dev.dantas.user.domain.entity.User;
import br.dev.dantas.user.domain.mappers.UserProfileMapper;
import br.dev.dantas.user.service.UserProfileService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = {IUserController.V1_PATH_DEFAULT})
@Log4j2
@RequiredArgsConstructor
public class UserProfileController implements IUserProfileController {

  private final UserProfileService userProfileService;
  private final UserProfileMapper mapper;

  @GetMapping
  @Override
  public ResponseEntity<List<UserProfileGetResponse>> list(
      @RequestParam(required = false) String name) {
    log.info("Request received to list all user profiles, param name '{}' ", name);

    var userProfiles = userProfileService.findAll();
    var userProfileGetResponses = mapper.toUserProfileGetResponseList(userProfiles);

    return ResponseEntity.ok(userProfileGetResponses);
  }

  @GetMapping("profiles/{id}/users")
  @Override
  public ResponseEntity<List<User>> listUsersByProfileId(@PathVariable Long id) {
    log.info("Request received to list all users by profile id, param id '{}' ", id);

    var userProfiles = userProfileService.findAllUsersByProfileId(id);

    return ResponseEntity.ok(userProfiles);
  }

  @GetMapping("{id}")
  @Override
  public ResponseEntity<UserProfileUsersGetResponse> findById(@PathVariable @Valid Long id) {
    log.info("Request received find profile by id '{}' ", id);

    var profile = userProfileService.findById(id);
    var profileGetResponse = mapper.toUserProfileUsersGetResponse(profile);

    return ResponseEntity.ok(profileGetResponse);
  }

  @PostMapping
  @Override
  public ResponseEntity<UserProfilePostResponse> save(@RequestBody @Valid UserProfilePostRequest request) {
    log.info("Request create user profile post method '{}' ", request);

    var userProfile = mapper.toUserProfile(request);
    userProfile = userProfileService.save(userProfile);
    var response = mapper.toUserProfilePostResponse(userProfile);

    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @PutMapping
  @Override
  public ResponseEntity<Void> update(@RequestBody @Valid UserProfilePutRequest request) {
    log.info("Request received to update the user profile '{}' ", request);

    var profileToUpdate = mapper.toUserProfile(request);
    userProfileService.update(profileToUpdate);

    return ResponseEntity.noContent().build();
  }

  @DeleteMapping("{id}")
  @Override
  public ResponseEntity<Void> deleteById(@PathVariable @Valid Long id) {
    log.info("Request received to delete the user profile by id'{}' ", id);

    userProfileService.delete(id);

    return ResponseEntity.noContent().build();
  }
}