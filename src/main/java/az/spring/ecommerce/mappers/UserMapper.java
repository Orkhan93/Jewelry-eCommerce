package az.spring.ecommerce.mappers;

import az.spring.ecommerce.model.User;
import az.spring.ecommerce.request.UserRequest;
import az.spring.ecommerce.request.UserSignUpRequest;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    User fromUserSignUpRequestToModel(UserSignUpRequest userSignUpRequest);

    UserRequest fromModelToUserRequest(User user);

}