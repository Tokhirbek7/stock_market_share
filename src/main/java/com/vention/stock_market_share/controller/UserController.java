package com.vention.stock_market_share.controller;

import com.vention.stock_market_share.exception.DuplicateEmailException;
import com.vention.stock_market_share.exception.InvalidInputException;
import com.vention.stock_market_share.exception.MissingEmailException;
import com.vention.stock_market_share.exception.UserAddException;
import com.vention.stock_market_share.model.User;
import com.vention.stock_market_share.service.EmailService;
import com.vention.stock_market_share.service.UserService;
import com.vention.stock_market_share.token.RegistrationTokenGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/users")
public class UserController {


    private final UserService userService;
    private final EmailService emailService;
    private final RegistrationTokenGenerator tokenGenerator;
    @Value("${registration.link}")
    private String basicLink;

    public UserController(UserService userService, EmailService emailService, RegistrationTokenGenerator tokenGenerator) {
        this.userService = userService;
        this.emailService = emailService;
        this.tokenGenerator = tokenGenerator;
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody User user) {
        if (user.getEmail()==null) {
            throw new MissingEmailException("Please check your input");
        }
        if (Objects.equals(userService.findByEmail(user.getEmail()), user.getEmail())) {
            throw new DuplicateEmailException("This email is already registered.");
        }
        long userId = userService.registerUser(user);
        String token = tokenGenerator.generateToken();
        String registrationLink = basicLink+token+userId;
        emailService.sendMailWithLink(user.getEmail(),registrationLink);
        return ResponseEntity.status(HttpStatus.OK).body("The user is registered successfully The link has been sent to this email");
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getUserById(id));
    }

    @PostMapping
    public ResponseEntity<String> addUser(@RequestBody User user) {
        long savedUserId = userService.addUser(user);
        if (savedUserId != 0) {
            return ResponseEntity.status(HttpStatus.CREATED).body("The user is added successfully");
        }

        throw new UserAddException("The user is not saved please check your input");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateUser(@PathVariable Long id, @RequestBody User user) {
        if(user.getEmail() == null) {
            user.setId(id);
            userService.updateUser(user);
            return ResponseEntity.status(HttpStatus.OK).body("The user with this " + id + " has been updated");
        }
        throw new InvalidInputException("Please check your input");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.status(HttpStatus.OK).body("The user with this " + id + " has been Deleted");
    }

    @DeleteMapping()
    private ResponseEntity<String> deleteAll() {
        userService.deleteAll();
        return ResponseEntity.status(HttpStatus.OK).body("All users have been Deleted");
    }
}
