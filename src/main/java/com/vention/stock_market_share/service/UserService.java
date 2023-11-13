package com.vention.stock_market_share.service;

import com.vention.stock_market_share.dto.SignUpRequest;
import com.vention.stock_market_share.dto.UserDto;
import com.vention.stock_market_share.exception.DataNotFoundException;
import com.vention.stock_market_share.exception.DuplicateEmailException;
import com.vention.stock_market_share.exception.DuplicateUsernameException;
import com.vention.stock_market_share.exception.InvalidInputException;
import com.vention.stock_market_share.model.SecurityInfo;
import com.vention.stock_market_share.model.User;
import com.vention.stock_market_share.repository.UserRepository;
import lombok.RequiredArgsConstructor;
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
    private final SecurityInfoService securityInfoService;

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
        return repository.delete(id);
    }

    public boolean deleteAll() {
        return repository.deleteAll() > 0;
    }

    public String getCodeViaEmail(String email) {
        String codeByEmail = repository.getCodeByEmail(email);
        if (codeByEmail == null) {
            throw new DataNotFoundException("code is not found");
        }
        return codeByEmail;
    }

    public User findByEmail(String email) {
        User byEmail = repository.findByEmail(email);
        if (byEmail == null) {
            throw new DataNotFoundException("user with this email " + email + " is not found");
        }
        return byEmail;
    }

    public boolean updateUserSetIsVerifiedToTrue(User user) {
        boolean isUpdated = repository.updateUserSetIsVerifiedToTrue(user);
        if (isUpdated) {
            user.setVerified(true);
            return true;
        }
        return false;
    }

    public boolean register(UserDto userDto) {
        long savedUserId = repository.registerUser(userDto);
        SecurityInfo securityInfo = new SecurityInfo();
        securityInfo.setUsername(userDto.getUsername());
        securityInfo.setPassword(userDto.getPassword());
        boolean isSaved = securityInfoService.createSecurityInfo(securityInfo, savedUserId);
        if (isSaved) {
            return emailService.sendMailWithCode(userDto.getEmail());
        }
        return false;
    }

    public boolean isInputValid(SignUpRequest request) {
        if (!isValidEmail(request.getEmail())) {
            throw new InvalidInputException("This email is not valid ");
        }
        User byEmail = repository.findByEmail(request.getEmail());
        if (byEmail != null && byEmail.getEmail().equals(request.getEmail())) {
            throw new DuplicateEmailException("This email is already registered.");
        }
        UserDto byUsername = repository.findByUsername(request.getUsername());
        if (byUsername != null) {
            throw new DuplicateUsernameException("This username is already taken");
        }
        if (!isPasswordValid(request.getPassword())) {
            throw new InvalidInputException("Password is not valid You need to enter at least one uppercase and one lowercase and one special character at least one digit");
        }
        return isPasswordValid(request.getPassword()) && isValidEmail(request.getEmail()) && byEmail == null;
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        return email.matches(emailRegex);
    }

    private boolean isPasswordValid(String password) {
        if (password.length() < 8) {
            return false;
        }
        return password.matches(".*[A-Z].*") &&
                password.matches(".*[a-z].*") &&
                password.matches(".*\\d.*") &&
                password.matches(".*[@#$%^&+=!].*");
    }

    public UserDto findByUsername(String username) {
        UserDto byUsername = repository.findByUsername(username);
        if (byUsername == null) {
            throw new DataNotFoundException("There is no user with this username: " + username);
        }
        return byUsername;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return repository.findByUsername(username);
    }
}
