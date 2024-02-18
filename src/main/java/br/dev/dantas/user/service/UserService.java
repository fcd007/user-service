package br.dev.dantas.user.service;

import br.dev.dantas.user.domain.entity.User;
import br.dev.dantas.user.repository.config.UserHardCodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserHardCodeRepository userHardCodeRepository;
    public List<User> listAll() {
        return userHardCodeRepository.findAll();
    }
}
