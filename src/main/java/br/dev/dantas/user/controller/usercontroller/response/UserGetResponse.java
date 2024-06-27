package br.dev.dantas.user.controller.usercontroller.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserGetResponse {

    private Long id;

    private String firstName;

    private String lastName;

    private String email;
}
