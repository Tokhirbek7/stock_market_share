package com.vention.stock_market_share.service;

import com.vention.stock_market_share.model.User;
import com.vention.stock_market_share.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long id) {
        return userRepository.findById(id);
    }

    public long addUser(User user) {
        return userRepository.save(user);
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

    public long registerUser(User user){
        return userRepository.registerUser(user);
    }
    public String findByEmail(String email){
        return userRepository.findByEmail(email);
    }
    public boolean isValidUser(User user){
        return userRepository.isValidInput(user);
    }
}
