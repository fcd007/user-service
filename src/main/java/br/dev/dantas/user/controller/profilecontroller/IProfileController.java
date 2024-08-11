package br.dev.dantas.user.controller.profilecontroller;

import br.dev.dantas.user.controller.profilecontroller.request.ProfilePostRequest;
import br.dev.dantas.user.controller.profilecontroller.request.ProfilePutRequest;
import br.dev.dantas.user.controller.profilecontroller.response.ProfileGetResponse;
import br.dev.dantas.user.controller.profilecontroller.response.ProfilePostResponse;
import io.swagger.v3.oas.annotations.Operation;
import java.util.List;
import org.springframework.http.ResponseEntity;

public interface IProfileController {

  public static final String V1_PATH_DEFAULT = "/api/v1/profiles";
  public static final String V1_PATH_OTHER = "/api/v1/profiles/";

  @Operation(summary = "List Profile")
  ResponseEntity<List<ProfileGetResponse>> listAllProfiles(String name);

  @Operation(summary = "Find by Id")
  ResponseEntity<ProfileGetResponse> findProfileById(Long id);

  @Operation(summary = "Save Profile")
  ResponseEntity<ProfilePostResponse> saveProfile(ProfilePostRequest request);

  @Operation(summary = "Delete Profile")
  ResponseEntity<Void> deleteProfileById(Long id);

  @Operation(summary = "Update Profile")
  ResponseEntity<Void> updateProfile(ProfilePutRequest request);
}
