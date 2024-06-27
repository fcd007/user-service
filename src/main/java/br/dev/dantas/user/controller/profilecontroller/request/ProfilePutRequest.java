package br.dev.dantas.user.controller.profilecontroller.request;

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
public class ProfilePutRequest {

  @NotNull
  private Long id;

  @NotBlank(message = "the field name is required")
  private String name;

  @NotBlank(message = "the field description is required")
  private String description;
}