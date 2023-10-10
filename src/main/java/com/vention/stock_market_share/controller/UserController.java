package com.vention.stock_market_share.controller;

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
        if (!userService.isValidUser(user))
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Please check your input");
        if (Objects.equals(userService.findByEmail(user.getEmail()), user.getEmail())) {
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
        long savedUserId = userService.addUser(user);
        if (savedUserId != 0)
            return ResponseEntity.status(HttpStatus.OK).body("The user is added successfully");

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("The user is not saved please check your input");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateUser(@PathVariable Long id, @RequestBody User user) {
        if(userService.isValidUser(user)) {
            user.setId(id);
            userService.updateUser(user);
            return ResponseEntity.status(HttpStatus.OK).body("The user with this " + id + " has been updated");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Please check your input");
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
