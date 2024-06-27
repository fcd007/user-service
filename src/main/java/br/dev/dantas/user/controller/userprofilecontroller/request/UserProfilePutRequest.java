package br.dev.dantas.user.controller.userprofilecontroller.request;

import br.dev.dantas.user.domain.entity.Profile;
import br.dev.dantas.user.domain.entity.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class UserProfilePutRequest {

  @NotNull
  private Long id;

  @NotBlank(message = "the field user is required")
  private User user;

  @NotBlank(message = "the field profile is required")
  private Profile profile;
}