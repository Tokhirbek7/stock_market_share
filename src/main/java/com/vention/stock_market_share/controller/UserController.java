package com.vention.stock_market_share.controller;

import com.vention.stock_market_share.exception.DataNotFoundException;
import com.vention.stock_market_share.exception.UserAddException;
import com.vention.stock_market_share.model.User;
import com.vention.stock_market_share.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
@PreAuthorize("hasAnyRole('ADMIN', 'USER')")
public class UserController {
    private final UserService userService;

    @PreAuthorize("hasAnyAuthority('admin:read', 'user:read')")
    @GetMapping
    public ResponseEntity<?> getAllUsers() {
        try {
            List<User> allUsers = userService.getAllUsers();
            return ResponseEntity.status(HttpStatus.OK).body(allUsers);
        } catch (DataNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No users found: " + ex.getMessage());
        }
    }

    @PreAuthorize("hasAnyAuthority('admin:read', 'user:read')")
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


    @PreAuthorize("hasAuthority('admin:create')")
    @PostMapping
    public ResponseEntity<HttpStatus> addUser(@RequestBody User user) {
        long savedUserId = userService.addUser(user);
        if (savedUserId != 0) {
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
        throw new UserAddException("The user " + user.getFirstname() + " is not saved. Please check your input.");
    }

    @PreAuthorize("hasAnyAuthority('admin:update', 'user:update')")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody User user) {
        try {
            if (Objects.equals(userService.getUserById(id).getId(), id)) {
                user.setId(id);
                userService.updateUser(user);
                return new ResponseEntity<>(HttpStatus.ACCEPTED);
            } else {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
        } catch (UserAddException ex) {
            log.error(ex.getMessage());
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PreAuthorize("hasAnyAuthority('admin:delete', 'user:delete')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        if (Objects.equals(userService.getUserById(id).getId(), id))
            if (userService.deleteUser(id)) {
                return ResponseEntity.status(HttpStatus.OK).body("The user with this " + id + " has been Deleted");
            }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("The user with this " + id + " not found");
    }

    @PreAuthorize("hasAuthority('admin:delete')")
    @DeleteMapping
    private ResponseEntity<?> deleteAll() {
        int numberOfDeletion = userService.deleteAll();
        if (numberOfDeletion > 0) {
            return ResponseEntity.status(HttpStatus.OK).body("All users have been Deleted");
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
