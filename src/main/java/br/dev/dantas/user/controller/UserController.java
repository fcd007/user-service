package br.dev.dantas.user.controller;

import br.dev.dantas.user.controller.request.UserPostRequest;
import br.dev.dantas.user.controller.request.UserPutRequest;
import br.dev.dantas.user.controller.response.UserGetResponse;
import br.dev.dantas.user.controller.response.UserPostResponse;
import br.dev.dantas.user.domain.mappers.UserMapper;
import br.dev.dantas.user.service.UserService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

  @GetMapping("{id}")
  public ResponseEntity<UserGetResponse> findById(@PathVariable Long id) {
    log.info("Request received find user by id '{}'", id);

    var user = userService.findById(id);
    var userGetResponse = userMapper.toUserGetResponse(user);

    return ResponseEntity.ok(userGetResponse);
  }

  @PostMapping
  public ResponseEntity<UserPostResponse> save(@RequestBody @Valid UserPostRequest request) {
    log.info("Request create user post method '{}'", request);

    var user = userMapper.toUser(request);
    user = userService.save(user);
    var response = userMapper.toUserPostResponse(user);

    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @DeleteMapping("{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    log.info("Request received to delete the user by id'{}'", id);

    userService.delete(id);
    return ResponseEntity.noContent().build();
  }

  @PutMapping
  public ResponseEntity<Void> update(@RequestBody @Valid UserPutRequest request) {
    log.info("Request received to update the user '{}'", request);

    var userToUpdate = userMapper.toUser(request);

    userService.update(userToUpdate);

    return ResponseEntity.noContent().build();
  }
}
