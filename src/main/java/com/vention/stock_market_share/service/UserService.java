package com.vention.stock_market_share.service;

import com.vention.stock_market_share.exception.DataNotFoundException;
import com.vention.stock_market_share.exception.DuplicateEmailException;
import com.vention.stock_market_share.model.User;
import com.vention.stock_market_share.repository.UserRepository;
import com.vention.stock_market_share.token.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository repository;
    private final EmailService emailService;
    private final TokenService tokenService;
    @Value("${registration.link}")
    private String basicLink;

    public List<User> getAllUsers() {
        List<User> allUsers = repository.findAll();
        if (allUsers.isEmpty()) {
            throw new DataNotFoundException("No user is found in database");
        }
        return allUsers;
    }

    public User getUserById(Long id) {
        return repository.findById(id);
    }

    public long addUser(User user) {
        return repository.save(user);
    }

    public boolean updateUser(User user) {
        return repository.update(user);
    }

    public boolean deleteUser(Long id) {
        return repository.delete(id) == 1;
    }

    public int deleteAll() {
        return repository.deleteAll();
    }

    public long registerUser(User user) {
        return repository.registerUser(user);
    }

    public boolean registerUserAndSendEmail(User user) {
        User byEmail = repository.findByEmail(user.getEmail());
        if (byEmail != null && byEmail.getEmail().equals(user.getEmail())) {
            throw new DuplicateEmailException("This email is already registered.");
        }
        long userId = registerUser(user);
        String token = tokenService.generateToken();
        tokenService.save(token);
        String url = basicLink + userId;
        return emailService.sendMailWithLink(user.getEmail(), token, url);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return repository.findByUsername(username);
    }
}
