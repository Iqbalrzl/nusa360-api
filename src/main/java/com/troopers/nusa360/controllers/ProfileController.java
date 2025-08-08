package com.troopers.nusa360.controllers;

import com.troopers.nusa360.dtos.UpdateUserRequest;
import com.troopers.nusa360.dtos.UserDto;
import com.troopers.nusa360.mappers.UserMapper;
import com.troopers.nusa360.models.Profile;
import com.troopers.nusa360.repositories.ProfileRepository;
import com.troopers.nusa360.repositories.UserRepository;
import com.troopers.nusa360.services.FileStorageService;
import com.troopers.nusa360.services.ProfileService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Map;

@RestController
@RequestMapping("/profile")
public class ProfileController {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final ProfileRepository profileRepository;
    private final ProfileService profileService;
    private final String publicDirectoryPath;

    public ProfileController(
            UserRepository userRepository,
            UserMapper userMapper,
            ProfileRepository profileRepository,
            ProfileService profileService,
            @Value("${spring.file.upload.public}") String publicDirectoryPath
    ){
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.profileRepository = profileRepository;
        this.profileService = profileService;
        this.publicDirectoryPath = publicDirectoryPath;
    }

    @GetMapping("/me")
    public ResponseEntity<UserDto> me(){
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        var userId  = (Long) authentication.getPrincipal();

        var user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        var userDto = userMapper.toUserDto(user);

        return ResponseEntity.ok(userDto);
    }

    @PatchMapping("/me")
    public ResponseEntity<?> updateUserProfilePartially (
            @RequestBody UpdateUserRequest request
    ){
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        var userId  = (Long) authentication.getPrincipal();

        var user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        if (request.getEmail() != null && !request.getEmail().equals(user.getEmail())) {
            if (userRepository.existsByEmail(request.getEmail())) {
                return ResponseEntity.badRequest().body(Map.of("email", "Email is already registered"));
            }
            user.setEmail(request.getEmail());
        }

        if (request.getUsername() != null) {
            user.setUsername(request.getUsername());
        }

        userRepository.save(user);

        return ResponseEntity.ok(userMapper.toUserDto(user));
    }

    @PutMapping("/me")
    public ResponseEntity<?> updateUserProfile (
            @RequestBody UpdateUserRequest request
    ){
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        var userId  = (Long) authentication.getPrincipal();

        var user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        if (request.getEmail() != null && !request.getEmail().equals(user.getEmail())) {
            if (userRepository.existsByEmail(request.getEmail())) {
                return ResponseEntity.badRequest().body(Map.of("email", "Email is already registered"));
            }
            user.setEmail(request.getEmail());
        }

        if (request.getUsername() != null) {
            user.setUsername(request.getUsername());
        }

        userRepository.save(user);

        return ResponseEntity.ok(userMapper.toUserDto(user));
    }

    @GetMapping("/avatar")
    public ResponseEntity<?> getAvatarUrl() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        var userId = (Long) authentication.getPrincipal();


        var profile = profileRepository.findById(userId).orElse(null);
        if (profile == null || profile.getAvatar_url() == null) {
            return ResponseEntity.notFound().build();
        }

        String avatarUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(publicDirectoryPath + "/")
                .path(profile.getAvatar_url())
                .toUriString();

        return ResponseEntity.ok(Map.of("avatarUrl", avatarUrl));
    }

    @PostMapping("avatar")
    public ResponseEntity<?> uploadAvatar(
            @RequestParam("file") MultipartFile file
    ) {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        var userId = (Long) authentication.getPrincipal();

        var profile = profileRepository.findById(userId).orElse(null);
        if (profile == null) {
            return ResponseEntity.notFound().build();
        }

        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "File is empty"));
        }

        var updatedProfile = profileService.updateAvatar(file,userId);

        String avatarUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(publicDirectoryPath + "/")
                .path(updatedProfile.getAvatar_url())
                .toUriString();


        return ResponseEntity.ok(Map.of("avatarUrl", avatarUrl));

    }

}
