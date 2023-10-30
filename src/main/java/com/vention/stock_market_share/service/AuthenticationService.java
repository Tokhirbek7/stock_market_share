package com.vention.stock_market_share.service;

import com.vention.stock_market_share.config.JwtService;
import com.vention.stock_market_share.enums.Role;
import com.vention.stock_market_share.model.User;
import com.vention.stock_market_share.repository.UserRepository;
import com.vention.stock_market_share.request.AuthenticationRequest;
import com.vention.stock_market_share.request.RegisterRequest;
import com.vention.stock_market_share.response.AuthenticationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) {
        User user = User.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .age(request.getAge())
                .role(Role.USER)
                .build();
        userRepository.registerUser(user);
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();

    }

    public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authenticationRequest.getEmail(),
                        authenticationRequest.getPassword()
                )
        );
        var user = new User();
        try {
            user = userRepository.findByEmail(authenticationRequest.getEmail());
        } catch (Exception e) {
            throw new UsernameNotFoundException("User is not found", e);
        }
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }
}
