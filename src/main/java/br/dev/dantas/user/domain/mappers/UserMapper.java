package br.dev.dantas.user.domain.mappers;

import br.dev.dantas.user.controller.request.UserPostRequest;
import br.dev.dantas.user.controller.request.UserPutRequest;
import br.dev.dantas.user.controller.response.UserGetResponse;
import br.dev.dantas.user.controller.response.UserPostResponse;
import br.dev.dantas.user.domain.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    User toUser(UserPostRequest request);

    User toUser(UserPutRequest request);

    List<UserGetResponse> toUserGetResponseList(List<User> users);

    UserGetResponse toUserGetResponse(User user);

    UserPostResponse toUserPostResponse(User user);
}
