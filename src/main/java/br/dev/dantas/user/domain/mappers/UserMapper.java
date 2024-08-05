package br.dev.dantas.user.domain.mappers;

import br.dev.dantas.user.annotation.EncodedMapping;
import br.dev.dantas.user.controller.usercontroller.request.UserPostRequest;
import br.dev.dantas.user.controller.usercontroller.request.UserPutRequest;
import br.dev.dantas.user.controller.usercontroller.response.UserGetResponse;
import br.dev.dantas.user.controller.usercontroller.response.UserPostResponse;
import br.dev.dantas.user.domain.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE,
    uses = PasswordEncodedMapper.class)
public interface UserMapper {

  @Mapping(target = "roles", constant = "ADMIN")
  @Mapping(target = "password", qualifiedBy = EncodedMapping.class)
  User toUser(UserPostRequest request);

  @Mapping(target = "password", qualifiedBy = EncodedMapping.class)
  User toUser(UserPutRequest request);

  List<UserGetResponse> toUserGetResponseList(List<User> users);

  UserGetResponse toUserGetResponse(User user);

  UserPostResponse toUserPostResponse(User user);
}
