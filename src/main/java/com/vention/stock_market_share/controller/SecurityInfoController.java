package com.vention.stock_market_share.controller;

import com.vention.stock_market_share.dto.SecurityInfoDTO;
import com.vention.stock_market_share.model.SecurityInfo;
import com.vention.stock_market_share.service.SecurityInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/security")
public class SecurityInfoController {

    private final SecurityInfoService securityInfoService;

    @Autowired
    public SecurityInfoController(SecurityInfoService securityInfoService) {
        this.securityInfoService = securityInfoService;
    }

    @PostMapping("/create")
    public void createSecurityInfo(@RequestBody SecurityInfo securityInfo) {
        securityInfoService.createSecurityInfo(securityInfo);
    }

    @GetMapping("/{id}")
    public SecurityInfoDTO getSecurityInfoById(@PathVariable Long id) {
        return securityInfoService.getSecurityInfoById(id);
    }

    @GetMapping("/all")
    public List<SecurityInfoDTO> getAllSecurityInfo() {
        return securityInfoService.getAllSecurityInfo();
    }

    @PutMapping("/update/{id}")
    public void updateSecurityInfo(@PathVariable Long id, @RequestBody SecurityInfoDTO securityInfoDTO) {
        securityInfoService.updateSecurityInfo(id, securityInfoDTO);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteSecurityInfo(@PathVariable Long id) {
        securityInfoService.deleteSecurityInfo(id);
    }
}
