package br.dev.dantas.user.controller.profilecontroller;

import br.dev.dantas.user.controller.profilecontroller.request.ProfilePostRequest;
import br.dev.dantas.user.controller.profilecontroller.request.ProfilePutRequest;
import br.dev.dantas.user.controller.profilecontroller.response.ProfileGetResponse;
import br.dev.dantas.user.controller.profilecontroller.response.ProfilePostResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.ResponseEntity;

public interface IProfileController {

  public static final String V1_PATH_DEFAULT = "/api/v1/profiles";
  public static final String V1_PATH_OTHER = "/api/v1/profiles/";

  @Tag(name = "List Profile", description = "Profile Controller")
  ResponseEntity<List<ProfileGetResponse>> list(String name);

  @Tag(name = "Find Profile", description = "Profile Controller")
  ResponseEntity<ProfileGetResponse> findById(Long id);

  @Tag(name = "Create Profile", description = "Profile Controller")
  ResponseEntity<ProfilePostResponse> save(ProfilePostRequest request);

  @Tag(name = "Delete Profile", description = "Delete profile by id")
  ResponseEntity<Void> deleteById(Long id);

  @Tag(name = "Update Profile", description = "Update profile")
  ResponseEntity<Void> update(ProfilePutRequest request);
}
