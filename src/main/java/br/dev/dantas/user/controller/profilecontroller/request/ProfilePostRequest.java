package br.dev.dantas.user.controller.profilecontroller.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ProfilePostRequest {

  @NotBlank(message = "the field name is required")
  private String name;

  @NotBlank(message = "the field description is required")
  private String description;
}
