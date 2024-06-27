package br.dev.dantas.user.domain.mappers;

import br.dev.dantas.user.controller.profilecontroller.request.ProfilePostRequest;
import br.dev.dantas.user.controller.profilecontroller.request.ProfilePutRequest;
import br.dev.dantas.user.controller.profilecontroller.response.ProfileGetResponse;
import br.dev.dantas.user.controller.profilecontroller.response.ProfilePostResponse;
import br.dev.dantas.user.domain.entity.Profile;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProfileMapper {

  Profile toProfile(ProfilePostRequest request);

  Profile toProfile(ProfilePutRequest request);

  ProfilePostResponse toProfilePostResponse(Profile profile);

  ProfileGetResponse toProfileGetResponse(Profile profile);

  List<ProfileGetResponse> toProfileGetResponseList(List<Profile> Profiles);
}
