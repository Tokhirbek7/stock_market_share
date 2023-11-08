package com.vention.stock_market_share.service;

import com.vention.stock_market_share.config.JwtService;
import com.vention.stock_market_share.dto.RefreshTokenRequest;
import com.vention.stock_market_share.dto.SignInRequest;
import com.vention.stock_market_share.dto.SignUpRequest;
import com.vention.stock_market_share.dto.UserDto;
import com.vention.stock_market_share.enums.Role;
import com.vention.stock_market_share.model.User;
import com.vention.stock_market_share.repository.UserRepository;
import com.vention.stock_market_share.response.AuthenticationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserService userService;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;


    public boolean register(SignUpRequest request) {
        User user = User.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .age(request.getAge())
                .role(Role.USER)
                .build();
        return userService.registerUserAndSendEmail(user);
    }

    public AuthenticationResponse authenticate(SignInRequest request) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword());
        UserDto byUsername = userRepository.findByUsername(request.getUsername());
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtService.generateToken(byUsername);
        String refreshToken = jwtService.generateRefreshToken(new HashMap<>(), byUsername);
        AuthenticationResponse authenticationResponse = new AuthenticationResponse();
        authenticationResponse.setToken(jwt);
        authenticationResponse.setRefreshToken(refreshToken);
        return authenticationResponse;
    }

    public AuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
        String username = jwtService.extractUsername(refreshTokenRequest.getToken());
        UserDto byUsername = userRepository.findByUsername(username);
        if (jwtService.isTokenValid(refreshTokenRequest.getToken(), byUsername)) {
            var jwt = jwtService.generateToken(byUsername);
            AuthenticationResponse authenticationResponse = new AuthenticationResponse();
            authenticationResponse.setToken(jwt);
            authenticationResponse.setRefreshToken(refreshTokenRequest.getToken());
            return authenticationResponse;
        }
        return null;
    }

    public Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            if (authentication.getPrincipal() instanceof UserDto user) {
                return user.getId();
            }
        }
        return null;
    }
}
