package br.dev.dantas.user.domain.mappers;

import br.dev.dantas.user.controller.userprofilecontroller.request.UserProfilePostRequest;
import br.dev.dantas.user.controller.userprofilecontroller.request.UserProfilePutRequest;
import br.dev.dantas.user.controller.userprofilecontroller.response.UserProfileGetResponse;
import br.dev.dantas.user.controller.userprofilecontroller.response.UserProfilePostResponse;
import br.dev.dantas.user.domain.entity.UserProfile;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserProfileMapper {

  UserProfile toUserProfile(UserProfilePostRequest request);

  UserProfile toUserProfile(UserProfilePutRequest request);

  UserProfilePostResponse toUserProfilePostResponse(UserProfile userProfile);

  UserProfileGetResponse toUserProfileGetResponse(UserProfile userProfile);

  List<UserProfileGetResponse> toUserProfileGetResponseList(List<UserProfile> userProfile);
}
