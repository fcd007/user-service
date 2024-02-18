package br.dev.dantas.user.controller.response;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserPostResponse {

    private String firstName;

    private String lastName;

    private String email;
}
