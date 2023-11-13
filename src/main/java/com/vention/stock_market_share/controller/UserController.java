package com.vention.stock_market_share.controller;

import com.vention.stock_market_share.exception.DataNotFoundException;
import com.vention.stock_market_share.exception.UserAddException;
import com.vention.stock_market_share.model.User;
import com.vention.stock_market_share.service.AuthenticationService;
import com.vention.stock_market_share.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;
    private final AuthenticationService authenticationService;

    @GetMapping("/all")
    public ResponseEntity<?> getAllUsers() {
        try {
            List<User> allUsers = userService.getAllUsers();
            return ResponseEntity.status(HttpStatus.OK).body(allUsers);
        } catch (DataNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No users found: " + ex.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<HttpStatus> getUserById(@PathVariable Long id) {
        User userById = userService.getUserById(id);
        if (userById == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if (Objects.equals(userService.getUserById(id).getId(), id)) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

    }

    @PostMapping
    public ResponseEntity<HttpStatus> addUser(@RequestBody User user) {
        long savedUserId = userService.addUser(user);
        if (savedUserId != 0) {
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
        throw new UserAddException("The user " + user.getFirstname() + " is not saved. Please check your input.");
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody User user) {
        try {
            if (Objects.equals(authenticationService.getCurrentUserId(), id)) {
                user.setId(id);
                boolean isUpdated = userService.updateUser(user);
                return isUpdated ? new ResponseEntity<>(HttpStatus.NO_CONTENT) : new ResponseEntity<>(HttpStatus.CONFLICT);
            } else {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
        } catch (UserAddException ex) {
            log.error(ex.getMessage());
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUserById(@PathVariable Long id) {
        if (Objects.equals(authenticationService.getCurrentUserId(), id)) {
            if (userService.deleteUser(id)) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("The user with this " + id + " not found");
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("The user with id: " + authenticationService.getCurrentUserId() + " does not have permission");
    }

    @DeleteMapping
    private ResponseEntity<?> deleteAll() {
        if (userService.deleteAll()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.CONFLICT);
    }
}
