package br.dev.dantas.user.commons;

import br.dev.dantas.user.domain.entity.User;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserUtils {
    public List<User> newUsersList() {
        var adele = User.builder().id(1L).firstName("Adele").lastName("Dinner").email("adele@gmail.com").build();
        var celine = User.builder().id(2L).firstName("Celine").lastName("Dion").email("celine@gmail.com").build();
        var laura = User.builder().id(3L).firstName("Laura").lastName("Pausini").email("laura@gmail.com").build();

        return new ArrayList<>(List.of(adele, celine, laura));
    }

    public User newUserToSave() {
        return User.builder().id(99L).firstName("Marilia").lastName("Mendonça").email("marilia.mendonca@gmail.com").build();
    }
}
