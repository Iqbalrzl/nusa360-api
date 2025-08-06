package com.troopers.nusa360.controllers;

import com.troopers.nusa360.dtos.UserDto;
import com.troopers.nusa360.mappers.UserMapper;
import com.troopers.nusa360.models.User;
import com.troopers.nusa360.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@AllArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @GetMapping("")
    public List<UserDto> getAllUsers(
            @RequestParam(required = false, defaultValue = "", name = "sort") String sort
    ) {
        if (!Set.of("username", "email").contains(sort)){
            sort = "username";
        }
        return userRepository.findAll(Sort.by(sort))
                .stream()
                .map(user -> userMapper.toUserDto(user))
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        var user = userRepository.findById(id).orElse(null);
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(userMapper.toUserDto(user));
    }
}
