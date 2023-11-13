package com.vention.stock_market_share.controller;

import com.vention.stock_market_share.dto.*;
import com.vention.stock_market_share.response.AuthenticationResponse;
import com.vention.stock_market_share.service.AuthenticationService;
import com.vention.stock_market_share.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody SignUpRequest request) {
        if (userService.isInputValid(request)) {
            authenticationService.register(request);
            return ResponseEntity.status(HttpStatus.CREATED).body("User registered please verify your email");
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<?> authenticate(
            @RequestBody SignInRequest request
    ) {
        UserDto byUsername = userService.findByUsername(request.getUsername());
        if (byUsername.isVerified()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(authenticationService.authenticate(request));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Please verify your email");
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody RefreshTokenRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authenticationService.refreshToken(request));
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verify(@RequestBody VerificationRequest request) {
        boolean verify = authenticationService.verify(request);
        return verify ? ResponseEntity.status(HttpStatus.CREATED).body("email is verified successfully") : ResponseEntity.status(HttpStatus.OK).body("already verified");
    }
}
