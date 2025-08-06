package com.troopers.nusa360.mappers;

import com.troopers.nusa360.dtos.UserDto;
import com.troopers.nusa360.models.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toUserDto(User user);
}

