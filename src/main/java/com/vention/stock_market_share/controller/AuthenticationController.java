package com.vention.stock_market_share.controller;

import com.vention.stock_market_share.dto.RefreshTokenRequest;
import com.vention.stock_market_share.dto.SignInRequest;
import com.vention.stock_market_share.dto.SignUpRequest;
import com.vention.stock_market_share.exception.DuplicateEmailException;
import com.vention.stock_market_share.exception.InvalidInputException;
import com.vention.stock_market_share.model.SecurityInfo;
import com.vention.stock_market_share.response.AuthenticationResponse;
import com.vention.stock_market_share.service.AuthenticationService;
import com.vention.stock_market_share.service.SecurityInfoService;
import com.vention.stock_market_share.token.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final SecurityInfoService securityInfoService;
    private final TokenService tokenService;

    @PostMapping("/register")
    public ResponseEntity<HttpStatus> registerUserAndSendEmail(@RequestBody SignUpRequest request) {
        if (request.getEmail() != null) {
            boolean isRegistered = authenticationService.register(request);
            return (isRegistered) ? new ResponseEntity<>(HttpStatus.OK) : new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody SignInRequest request
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(authenticationService.authenticate(request));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody RefreshTokenRequest request
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(authenticationService.refreshToken(request));
    }

    @PostMapping("/create/{userId}")
    public ResponseEntity<HttpStatus> createLoginInfo(@PathVariable Long userId, @RequestBody SecurityInfo securityInfo, @RequestParam String token) {
        if (!securityInfoService.isValid(securityInfo)) {
            throw new InvalidInputException("Please check your input");
        }
        if (tokenService.findToken().getBody().equals(token)) {
            return getHttpStatusResponseEntity(userId, securityInfo, securityInfoService);
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    static ResponseEntity<HttpStatus> getHttpStatusResponseEntity(@PathVariable Long userId, @RequestBody SecurityInfo securityInfo, SecurityInfoService securityInfoService) {
        SecurityInfo byUsername = securityInfoService.findByUsername(securityInfo.getUsername());
        if (byUsername != null) {
            throw new DuplicateEmailException("This email is already registered.");
        }
        boolean saved = securityInfoService.createSecurityInfo(securityInfo, userId);
        return (saved) ? new ResponseEntity<>(HttpStatus.CREATED) : new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
