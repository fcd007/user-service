package br.dev.dantas.user.controller.request;


import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserPostRequest {

    @NotBlank(message = "he field firstName is required")
    private String firstName;

    @NotBlank(message = "he field lastName is required")
    private String lastName;

    @NotBlank(message = "he field email is required")
    private String email;
}
