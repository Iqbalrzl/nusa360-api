package com.troopers.nusa360.mappers;

import com.troopers.nusa360.dtos.RegisterUserRequest;
import com.troopers.nusa360.dtos.UpdateUserRequest;
import com.troopers.nusa360.dtos.UserDto;
import com.troopers.nusa360.models.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toUserDto(User user);
    User toEntity(RegisterUserRequest request);
    void updateUser(UpdateUserRequest request, @MappingTarget User user);
}

