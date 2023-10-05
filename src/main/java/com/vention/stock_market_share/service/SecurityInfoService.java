package com.vention.stock_market_share.service;

import com.vention.stock_market_share.model.SecurityInfo;
import com.vention.stock_market_share.repository.SecurityInfoRepository;
import com.vention.stock_market_share.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SecurityInfoService {

    private final SecurityInfoRepository securityInfoRepository;
    private final UserRepository userRepository;

    @Autowired
    public SecurityInfoService(SecurityInfoRepository securityInfoRepository, UserRepository userRepository) {
        this.securityInfoRepository = securityInfoRepository;
        this.userRepository = userRepository;
    }

    public void createSecurityInfo(SecurityInfo securityInfo, long userId) {
        securityInfoRepository.save(securityInfo, userId);
    }

    public SecurityInfo getSecurityInfoById(Long id) {
        return securityInfoRepository.findById(id);
    }

    public List<SecurityInfo> getAllSecurityInfo() {
        return securityInfoRepository.findAll();
    }

    public void updateSecurityInfo(Long id, SecurityInfo securityInfoDTO) {
        SecurityInfo existingSecurityInfo = securityInfoRepository.findById(id);
        if (existingSecurityInfo != null) {
            securityInfoDTO.setId(id);
            securityInfoRepository.update(securityInfoDTO);
        }
    }

    public String findByUsername(String username){
        return securityInfoRepository.findByUsername(username);
    }

    public void deleteSecurityInfo(Long id) {
        securityInfoRepository.delete(id);
    }

    public boolean isValid(SecurityInfo securityInfo){
        return securityInfoRepository.isValidInput(securityInfo);
    }



}
