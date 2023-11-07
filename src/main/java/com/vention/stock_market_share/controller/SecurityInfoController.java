package com.vention.stock_market_share.controller;

import com.vention.stock_market_share.exception.InvalidInputException;
import com.vention.stock_market_share.model.SecurityInfo;
import com.vention.stock_market_share.service.SecurityInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.vention.stock_market_share.controller.AuthenticationController.getHttpStatusResponseEntity;

@RestController
@RequestMapping("/security")
@RequiredArgsConstructor
public class SecurityInfoController {
    private final SecurityInfoService securityInfoService;

    @PostMapping("/{userId}")
    public ResponseEntity<HttpStatus> createSecurityInfo(@PathVariable Long userId, @RequestBody SecurityInfo securityInfo) {
        if (!securityInfoService.isValid(securityInfo)) {
            throw new InvalidInputException("Please check your input");
        }
        return getHttpStatusResponseEntity(userId, securityInfo, securityInfoService);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SecurityInfo> getSecurityInfoById(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(securityInfoService.getSecurityInfoById(id));
    }

    @GetMapping
    public ResponseEntity<List<SecurityInfo>> getAllSecurityInfo() {
        return ResponseEntity.status(HttpStatus.OK).body(securityInfoService.getAllSecurityInfo());
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateSecurityInfo(@PathVariable Long id, @RequestBody SecurityInfo securityInfo) {
        if (securityInfoService.isValid(securityInfo)) {
            securityInfoService.updateSecurityInfo(id, securityInfo);
            return ResponseEntity.status(HttpStatus.OK).body("Updated successfully");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Please check your input");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteSecurityInfo(@PathVariable Long id) {
        securityInfoService.deleteSecurityInfo(id);
        return ResponseEntity.status(HttpStatus.OK).body("A user has been Deleted");
    }
}
