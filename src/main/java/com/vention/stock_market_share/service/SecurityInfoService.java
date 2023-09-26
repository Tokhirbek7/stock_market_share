package com.vention.stock_market_share.service;

import com.vention.stock_market_share.dto.SecurityInfoDTO;
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

    public void createSecurityInfo(SecurityInfo securityInfo) {
        securityInfoRepository.save(securityInfo);
    }

    public SecurityInfoDTO getSecurityInfoById(Long id) {
        return securityInfoRepository.findById(id);
    }

    public List<SecurityInfoDTO> getAllSecurityInfo() {
        return securityInfoRepository.findAll();
    }

    public void updateSecurityInfo(Long id, SecurityInfoDTO securityInfoDTO) {
        SecurityInfoDTO existingSecurityInfo = securityInfoRepository.findById(id);
        if (existingSecurityInfo != null) {
            securityInfoDTO.setId(id);
            securityInfoRepository.update(securityInfoDTO);
        }
    }

    public void deleteSecurityInfo(Long id) {
        securityInfoRepository.delete(id);
    }



}
