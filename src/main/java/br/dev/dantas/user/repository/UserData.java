package br.dev.dantas.user.repository;

import br.dev.dantas.user.domain.entity.User;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import org.springframework.stereotype.Component;

@Getter
@Component
public class UserData {

  private List<User> users = new ArrayList<>();

  {
    var adele = User.builder().id(1L).firstName("Adele").lastName("Dinner").email("adele@gmail.com")
        .build();
    var celine = User.builder().id(2L).firstName("Celine").lastName("Dion")
        .email("celine@gmail.com").build();
    var laura = User.builder().id(3L).firstName("Laura").lastName("Pausini")
        .email("laura@gmail.com").build();

    users.addAll(List.of(adele, celine, laura));
  }

}
