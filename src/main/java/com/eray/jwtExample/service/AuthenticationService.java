package com.eray.jwtExample.service;

import com.eray.jwtExample.dto.UserDto;
import com.eray.jwtExample.dto.UserRequest;
import com.eray.jwtExample.dto.UserResponse;
import com.eray.jwtExample.enums.Role;
import com.eray.jwtExample.model.User;
import com.eray.jwtExample.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service

public class AuthenticationService {

    private final UserRepository userRepository;

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;

    public AuthenticationService(UserRepository userRepository, JwtService jwtService, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    public UserResponse save(UserDto userDto) {
        User user = User.builder().email(userDto.getEmail())
                .password(userDto.getPassword())
                .name(userDto.getName()).surname(userDto.getSurname())
                .role(Role.USER).build();
        userRepository.save(user);

        var token =  jwtService.generateToken(user);

        return UserResponse.builder().token(token).build();
    }


    public UserResponse auth(UserRequest userRequest) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userRequest.getEmail(), userRequest.getPassword())); // userRequest içerisindeki email ve passwordu vererek authenticate ederek doğru mu ona baktık
        User user = userRepository.findByEmail(userRequest.getEmail()).orElseThrow(); // user requestin emailini alıp
        String token = jwtService.generateToken(user);          // authenticate olan user ile bir token olusturduk 
        return UserResponse.builder().token(token).build();
    }
}
