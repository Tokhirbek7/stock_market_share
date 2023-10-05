package com.vention.stock_market_share.controller;

import com.vention.stock_market_share.dto.UserRegistrationDTO;
import com.vention.stock_market_share.model.User;
import com.vention.stock_market_share.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/users")
public class UserController {


    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody User user) {
        if (Objects.equals(userService.findByEmail(user.getEmail()), user.getEmail())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("This email is already registered. Please use a different email.");
        }
        userService.registerUser(user);
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
        userService.addUser(user);
        return ResponseEntity.status(HttpStatus.OK).body("The user is added successfully");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateUser(@PathVariable Long id, @RequestBody User user) {
        user.setId(id);
        userService.updateUser(user);
        return ResponseEntity.status(HttpStatus.OK).body("The user with this " + id+ " has been updated");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.status(HttpStatus.OK).body("The user with this " + id+ " has been Deleted");
    }
    @DeleteMapping()
    private ResponseEntity<String> deleteAll(){
        userService.deleteAll();
        return ResponseEntity.status(HttpStatus.OK).body("All users have been Deleted");
    }
}
