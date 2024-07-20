package br.dev.dantas.user.controller.profilecontroller;

import static br.dev.dantas.user.controller.profilecontroller.IProfileController.V1_PATH_DEFAULT;
import static br.dev.dantas.user.controller.profilecontroller.IProfileController.V1_PATH_OTHER;

import br.dev.dantas.user.controller.profilecontroller.request.ProfilePostRequest;
import br.dev.dantas.user.controller.profilecontroller.request.ProfilePutRequest;
import br.dev.dantas.user.controller.profilecontroller.response.ProfileGetResponse;
import br.dev.dantas.user.controller.profilecontroller.response.ProfilePostResponse;
import br.dev.dantas.user.domain.mappers.ProfileMapper;
import br.dev.dantas.user.service.ProfileService;
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
@RequestMapping(path = {V1_PATH_DEFAULT, V1_PATH_OTHER})
@Log4j2
@RequiredArgsConstructor
public class ProfileController implements IProfileController{

  private final ProfileService profileService;
  private final ProfileMapper mapper;

  @GetMapping
  public ResponseEntity<List<ProfileGetResponse>> list(
      @RequestParam(required = false) String name) {
    log.info("Request received to list all profiles, param name '{}' ", name);

    var profiles = profileService.findAll(name);
    var profileGetResponses = mapper.toProfileGetResponseList(profiles);

    return ResponseEntity.ok(profileGetResponses);
  }

  @GetMapping("{id}")
  public ResponseEntity<ProfileGetResponse> findById(@PathVariable @Valid Long id) {
    log.info("Request received find profile by id '{}' ", id);

    var profile = profileService.findById(id);
    var profileGetResponse = mapper.toProfileGetResponse(profile);

    return ResponseEntity.ok(profileGetResponse);
  }

  @PostMapping
  public ResponseEntity<ProfilePostResponse> save(@RequestBody @Valid ProfilePostRequest request) {
    log.info("Request create profile post method '{}' ", request);

    var profile = mapper.toProfile(request);
    profile = profileService.save(profile);
    var response = mapper.toProfilePostResponse(profile);

    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @DeleteMapping("{id}")
  public ResponseEntity<Void> deleteById(@PathVariable @Valid Long id) {
    log.info("Request received to delete the profile by id'{}' ", id);

    profileService.delete(id);

    return ResponseEntity.noContent().build();
  }

  @PutMapping
  public ResponseEntity<Void> update(@RequestBody @Valid ProfilePutRequest request) {
    log.info("Request received to update the profile '{}' ", request);

    var profileToUpdate = mapper.toProfile(request);
    profileService.update(profileToUpdate);

    return ResponseEntity.noContent().build();
  }
}