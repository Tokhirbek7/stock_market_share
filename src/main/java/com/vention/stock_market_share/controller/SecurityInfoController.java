package com.vention.stock_market_share.controller;

import com.vention.stock_market_share.model.SecurityInfo;
import com.vention.stock_market_share.model.User;
import com.vention.stock_market_share.service.SecurityInfoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/security")
public class SecurityInfoController {

    private final SecurityInfoService securityInfoService;


    public SecurityInfoController(SecurityInfoService securityInfoService) {
        this.securityInfoService = securityInfoService;
    }

    @PostMapping("/{userId}")
    public ResponseEntity<String> createSecurityInfo(@PathVariable Long userId, @RequestBody SecurityInfo securityInfo) {
        if (Objects.equals(securityInfoService.findByUsername(securityInfo.getUsername()), securityInfo.getUsername())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("This username is already taken");
        }
        securityInfoService.createSecurityInfo(securityInfo, userId);
        return ResponseEntity.status(HttpStatus.OK).body("OK");
    }

    @GetMapping("/{id}")
    public ResponseEntity<SecurityInfo> getSecurityInfoById(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(securityInfoService.getSecurityInfoById(id));
    }

    @GetMapping()
    public ResponseEntity<List<SecurityInfo>> getAllSecurityInfo() {
        return ResponseEntity.status(HttpStatus.OK).body(securityInfoService.getAllSecurityInfo());
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateSecurityInfo(@PathVariable Long id, @RequestBody SecurityInfo securityInfo) {
        securityInfoService.updateSecurityInfo(id, securityInfo);
        return ResponseEntity.status(HttpStatus.OK).body("Updated successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteSecurityInfo(@PathVariable Long id) {
        securityInfoService.deleteSecurityInfo(id);
        return ResponseEntity.status(HttpStatus.OK).body("A user has been Deleted");
    }
}
