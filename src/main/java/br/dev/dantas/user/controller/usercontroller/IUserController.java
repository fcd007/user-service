package br.dev.dantas.user.controller.usercontroller;

import br.dev.dantas.user.controller.usercontroller.request.UserPostRequest;
import br.dev.dantas.user.controller.usercontroller.request.UserPutRequest;
import br.dev.dantas.user.controller.usercontroller.response.UserGetResponse;
import br.dev.dantas.user.controller.usercontroller.response.UserPostResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.ResponseEntity;

@Tag(name = "User API", description = "User API")
public interface IUserController {

  public static final String V1_PATH_DEFAULT = "/api/v1/users";

  public static final String V1_PATH_OTHER = "/api/v1/users/";

  @Operation(summary = "List all users")
  ResponseEntity<List<UserGetResponse>> list();

  @Operation(summary = "Find user by id")
  ResponseEntity<UserGetResponse> findById(Long id);

  @Operation(summary = "Create user")
  ResponseEntity<UserPostResponse> save(UserPostRequest request);

  @Operation(summary = "Delete user by id")
  ResponseEntity<Void> delete(Long id);

  @Operation(summary = "Update user")
  ResponseEntity<Void> update(UserPutRequest request);
}
