package br.dev.dantas.user.controller.usercontroller.request;


import br.dev.dantas.user.utils.StringUtility;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserPostRequest {

  @NotBlank(message = "the field firstName is required")
  private String firstName;

  @NotBlank(message = "the field lastName is required")
  private String lastName;

  @Email(regexp = StringUtility.VALIDAR_EMAIL_REGEX_RFC_5322, message = "the email format is not valid")
  private String email;
}
