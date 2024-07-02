package br.dev.dantas.user.controller.userprofilecontroller.response;

import br.dev.dantas.user.domain.entity.Profile;
import br.dev.dantas.user.domain.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class UserProfileUsersGetResponse {

  private Long id;

  private User user;

  private Profile profile;
}