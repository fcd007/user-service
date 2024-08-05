package br.dev.dantas.user.commons;

import br.dev.dantas.user.domain.entity.User;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserUtils {

  public List<User> newUsersList() {
    var adele = User.builder().id(1L).firstName("Adele").lastName("Dinner").email("adele@gmail.com")
        .password("{bcrypt}$2a$10$hPbciUrbIEZw6J5Utie.HOjh2h2XeaEm4w3KU5aYeuBD3VpdReFmS")
        .roles("USER").build();
    var celine = User.builder().id(2L).firstName("Celine").lastName("Dion")
        .email("celine@gmail.com")
        .password("{bcrypt}$2a$10$hPbciUrbIEZw6J5Utie.HOjh2h2XeaEm4w3KU5aYeuBD3VpdReFmS")
        .roles("USER").build();
    var laura = User.builder().id(3L).firstName("Laura").lastName("Pausini")
        .email("laura@gmail.com")
        .password("{bcrypt}$2a$10$hPbciUrbIEZw6J5Utie.HOjh2h2XeaEm4w3KU5aYeuBD3VpdReFmS")
        .roles("USER").build();

    return new ArrayList<>(List.of(adele, celine, laura));
  }

  public User newUserToSave() {
    return User.builder().id(99L).firstName("Marilia").lastName("Mendonça")
        .email("marilia.mendonca@gmail.com")
        .password("{bcrypt}$2a$10$hPbciUrbIEZw6J5Utie.HOjh2h2XeaEm4w3KU5aYeuBD3VpdReFmS")
        .roles("USER").build();
  }

  public User newUserSaved() {
    return User.builder().id(99L).firstName("Julios").lastName("Asteca")
        .email("julio.asteca@gmail.com")
        .password("{bcrypt}$2a$10$hPbciUrbIEZw6J5Utie.HOjh2h2XeaEm4w3KU5aYeuBD3VpdReFmS")
        .roles("USER").build();
  }
}
