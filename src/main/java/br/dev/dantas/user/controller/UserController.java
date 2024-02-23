package br.dev.dantas.user.controller;


import br.dev.dantas.user.controller.response.UserGetResponse;
import br.dev.dantas.user.domain.mappers.UserMapper;
import br.dev.dantas.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = {IUserController.V1_PATH_DEFAULT, IUserController.V1_PATH_OTHER})
@Log4j2
@RequiredArgsConstructor
public class UserController {

    private final UserMapper userMapper;
    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<UserGetResponse>> list() {
        log.info("Request received to list all users");

        var users = userService.findAll();
        var userGetResponse = userMapper.toUserGetResponseList(users);

        return ResponseEntity.ok(userGetResponse);
    }
}
