package com.troopers.nusa360.controllers;

import com.troopers.nusa360.config.JwtConfig;
import com.troopers.nusa360.dtos.JwtResponse;
import com.troopers.nusa360.dtos.LoginRequest;
import com.troopers.nusa360.dtos.UserDto;
import com.troopers.nusa360.mappers.UserMapper;
import com.troopers.nusa360.models.User;
import com.troopers.nusa360.repositories.UserRepository;
import com.troopers.nusa360.services.JwtService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final JwtConfig jwtConfig;
    private final UserMapper userMapper;

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

    @PostMapping("refresh")
    public ResponseEntity<JwtResponse> refresh(
            @CookieValue(value = "refreshToken") String refreshToken
    ){
       if  (!jwtService.validateToken(refreshToken)){
           return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
       };

       var userId = jwtService.getUserIdFromToken(refreshToken);
       var user = userRepository.findById(userId).orElseThrow();
       var accessToken = jwtService.generateAccessToken(user);

       return ResponseEntity.ok(new JwtResponse(accessToken));
    }

    @PostMapping("/validate")
    public boolean validateToken(@RequestHeader("Authorization") String authHeader) {
        var token = authHeader.replace("Bearer ", "");

        return jwtService.validateToken(token);
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(
            @Valid @RequestBody LoginRequest request,
            HttpServletResponse response
    ) {

//        If not using Authentication Manager
//        var user =  userRepository.findByEmail(request.getEmail()).orElse(null);
//        if (user == null){
//            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
//        }
//        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())){
//            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
//        };

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        var user = userRepository.findByEmail(request.getEmail()).orElseThrow();

        var accessToken = jwtService.generateAccessToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);

        var cookie = new Cookie("refreshToken", refreshToken);
        cookie.setHttpOnly(true);
        cookie.setPath("/auth/refresh");
        cookie.setMaxAge(jwtConfig.getRefreshTokenExpiration()); // 7 days
        cookie.setSecure(true);
        response.addCookie(cookie);

        return ResponseEntity.ok(new JwtResponse(accessToken));
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Void> handleBadCredentialsException(){
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
}
