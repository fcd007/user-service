package br.dev.dantas.user.controller.profilecontroller.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class ProfileGetResponse {

  private Long id;

  private String name;
}