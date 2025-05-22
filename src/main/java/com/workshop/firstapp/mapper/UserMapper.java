package com.workshop.firstapp.mapper;

import com.workshop.firstapp.model.User;
import com.workshop.firstapp.model.UserRequest;
import com.workshop.firstapp.model.UserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    // Entity to DTO
    UserResponse userToUserResponse(User user);

    // DTO to Entity
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", constant = "ACTIVE")
    User userRequestToUser(UserRequest userRequest);

    // List mapping
    List<UserResponse> usersToUserResponses(List<User> users);
}