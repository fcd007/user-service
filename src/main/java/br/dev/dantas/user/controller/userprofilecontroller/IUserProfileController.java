package br.dev.dantas.user.controller.userprofilecontroller;

import br.dev.dantas.user.controller.userprofilecontroller.request.UserProfilePostRequest;
import br.dev.dantas.user.controller.userprofilecontroller.request.UserProfilePutRequest;
import br.dev.dantas.user.controller.userprofilecontroller.response.UserProfileGetResponse;
import br.dev.dantas.user.controller.userprofilecontroller.response.UserProfilePostResponse;
import br.dev.dantas.user.controller.userprofilecontroller.response.UserProfileUsersGetResponse;
import br.dev.dantas.user.domain.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.ResponseEntity;

@Tag(name = "User Profile API", description = "User related endpoints")
public interface IUserProfileController {

  public static final String V1_PATH_DEFAULT = "/api/v1/user-profiles";
  public static final String V1_PATH_OTHER = "/api/v1/user-profiles/";

  @Operation(summary = "List all user profiles", description = "List all user profiles")
  ResponseEntity<List<UserProfileGetResponse>> list(String name);

  @Operation(summary = "List all users profiles by profile id", description = "List all users profiles by profile id")
  ResponseEntity<List<User>> listUsersByProfileId(Long id);

  @Operation(summary = "Find user profile by id", description = "Find user profile by id")
  ResponseEntity<UserProfileUsersGetResponse> findById(Long id);

  @Operation(summary = "Save user profile", description = "Save user profile")
  ResponseEntity<UserProfilePostResponse> save(UserProfilePostRequest request);

  @Operation(summary = "Update user profile", description = "Update user profile")
  ResponseEntity<Void> update(UserProfilePutRequest request);

  @Operation(summary = "Delete user profile by id", description = "Delete user profile by id")
  ResponseEntity<Void> deleteById(Long id);

}
