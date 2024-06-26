package br.dev.dantas.user.controller.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserGetRequest {

  private Long id;

  private String firstName;

  private String lastName;

  private String email;
}
