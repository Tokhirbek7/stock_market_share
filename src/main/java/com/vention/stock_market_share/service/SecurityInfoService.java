package com.vention.stock_market_share.service;

import com.vention.stock_market_share.model.SecurityInfo;
import com.vention.stock_market_share.repository.SecurityInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SecurityInfoService {
    private final SecurityInfoRepository securityInfoRepository;

    public boolean createSecurityInfo(SecurityInfo securityInfo, long userId) {
        return securityInfoRepository.save(securityInfo, userId);
    }

    public SecurityInfo getSecurityInfoById(Long id) {
        return securityInfoRepository.findById(id);
    }

    public List<SecurityInfo> getAllSecurityInfo() {
        return securityInfoRepository.findAll();
    }

    public boolean updateSecurityInfo(Long id, SecurityInfo securityInfoDTO) {
        return securityInfoRepository.update(id, securityInfoDTO);
    }

    public SecurityInfo findByUsername(String username) {
        return securityInfoRepository.findByUsername(username);
    }

    public boolean deleteSecurityInfo(Long userId) {
        return securityInfoRepository.delete(userId);
    }

    public boolean isValid(SecurityInfo securityInfo) {
        return securityInfoRepository.isValidInput(securityInfo);
    }
}
