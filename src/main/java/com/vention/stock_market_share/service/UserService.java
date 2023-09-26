package com.vention.stock_market_share.service;

import com.vention.stock_market_share.dto.UserDTO;
import com.vention.stock_market_share.dto.UserRegistrationDTO;
import com.vention.stock_market_share.model.User;
import com.vention.stock_market_share.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public List<UserDTO> getAllUsers() {
        return userRepository.findAll();
    }

    public UserDTO getUserById(Long id) {
        return userRepository.findById(id);
    }

    public void addUser(User user) {
        userRepository.save(user);
    }

    public void updateUser(User user) {
        userRepository.update(user);
    }

    public void deleteUser(Long id) {
        userRepository.delete(id);
    }

    public void deleteAll(){
        userRepository.deleteAll();
    }

    public void registerUser(UserRegistrationDTO userRegistrationDTO){
        userRepository.registerUser(userRegistrationDTO);
    }
}
