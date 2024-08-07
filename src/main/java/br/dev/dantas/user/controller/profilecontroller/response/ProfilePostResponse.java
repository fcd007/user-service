package br.dev.dantas.user.controller.profilecontroller.response;

import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.With;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@With
public class ProfilePostResponse {

  @NotNull
  private Long id;

  @NotBlank(message = "the field name is required")
  private String name;

  @NotBlank(message = "the field description is required")
  private String description;
}