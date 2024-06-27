package br.dev.dantas.user.controller.userprofilecontroller.request;

import br.dev.dantas.user.domain.entity.Profile;
import br.dev.dantas.user.domain.entity.User;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserProfilePostRequest {

  private User user;

  private Profile profile;
}
