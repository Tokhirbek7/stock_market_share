package com.vention.stock_market_share.controller;

import com.vention.stock_market_share.exception.DuplicateEmailException;
import com.vention.stock_market_share.exception.InvalidInputException;
import com.vention.stock_market_share.model.SecurityInfo;
import com.vention.stock_market_share.service.AuthenticationService;
import com.vention.stock_market_share.service.SecurityInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping("/security")
@RequiredArgsConstructor
public class SecurityInfoController {
    private final SecurityInfoService securityInfoService;
    private final AuthenticationService authenticationService;

    @PostMapping("/{userId}")
    public ResponseEntity<HttpStatus> createSecurityInfo(@PathVariable Long userId, @RequestBody SecurityInfo securityInfo) {
        if (!securityInfoService.isValid(securityInfo)) {
            throw new InvalidInputException("Please check your input");
        }
        SecurityInfo byUsername = securityInfoService.findByUsername(securityInfo.getUsername());
        if (byUsername != null) {
            throw new DuplicateEmailException("This email is already registered.");
        }
        boolean saved = securityInfoService.createSecurityInfo(securityInfo, userId);
        return (saved) ? new ResponseEntity<>(HttpStatus.CREATED) : new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getSecurityInfoById(@PathVariable Long id) {
        if (Objects.equals(authenticationService.getCurrentUserId(), id)) {
            SecurityInfo securityInfoById = securityInfoService.getSecurityInfoById(id);
            return securityInfoById == null ? new ResponseEntity<>(HttpStatus.NOT_FOUND) : ResponseEntity.status(HttpStatus.OK).body(securityInfoById);
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllSecurityInfo() {
        return ResponseEntity.status(HttpStatus.OK).body(securityInfoService.getAllSecurityInfo());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateSecurityInfo(@PathVariable Long id, @RequestBody SecurityInfo securityInfo) {
        if (Objects.equals(authenticationService.getCurrentUserId(), id)) {
            if (securityInfoService.isValid(securityInfo)) {
                boolean isUpdated = securityInfoService.updateSecurityInfo(id, securityInfo);
                return isUpdated ? ResponseEntity.status(HttpStatus.OK).body("Updated successfully") : new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Please check your input");
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSecurityInfoById(@PathVariable Long id) {
        boolean isDeleted = securityInfoService.deleteSecurityInfo(id);
        return isDeleted ? ResponseEntity.status(HttpStatus.OK).body("A user has been Deleted") : new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
