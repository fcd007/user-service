package br.dev.dantas.user.domain.entity;

import lombok.*;

@Getter
@Setter
@Builder
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class User {

    @EqualsAndHashCode.Include
    private Long id;

    private String firstName;

    private String lastName;

    private String email;
}
