package org.fakultet.vjezba3.controller;

import org.fakultet.vjezba3.model.User;
import org.fakultet.vjezba3.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserRestController {
    private final UserService userService;

    public UserRestController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return userService.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<User> getUserByUsername(@PathVariable String username) {
        return userService.findByUsername(username)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody User user) {
        try {
            if (userService.existsByUsername(user.getUsername())) {
                return ResponseEntity.badRequest()
                    .body(createErrorResponse("Username already exists"));
            }
            if (userService.existsByEmail(user.getEmail())) {
                return ResponseEntity.badRequest()
                    .body(createErrorResponse("Email already exists"));
            }
            User savedUser = userService.save(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(createErrorResponse("Failed to create user: " + e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody User user) {
        try {
            User updatedUser = userService.update(id, user);
            return ResponseEntity.ok(updatedUser);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(createErrorResponse("Failed to update user: " + e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        try {
            if (userService.findById(id).isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            userService.deleteById(id);
            return ResponseEntity.ok(createSuccessResponse("User deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(createErrorResponse("Failed to delete user: " + e.getMessage()));
        }
    }

    @PostMapping("/{userId}/favorites/{albumId}")
    public ResponseEntity<?> addFavoriteAlbum(@PathVariable Long userId, @PathVariable Long albumId) {
        try {
            User user = userService.addFavoriteAlbum(userId, albumId);
            return ResponseEntity.ok(user);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                .body(createErrorResponse(e.getMessage()));
        }
    }

    @DeleteMapping("/{userId}/favorites/{albumId}")
    public ResponseEntity<?> removeFavoriteAlbum(@PathVariable Long userId, @PathVariable Long albumId) {
        try {
            User user = userService.removeFavoriteAlbum(userId, albumId);
            return ResponseEntity.ok(user);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                .body(createErrorResponse(e.getMessage()));
        }
    }

    private Map<String, String> createErrorResponse(String message) {
        Map<String, String> response = new HashMap<>();
        response.put("error", message);
        return response;
    }

    private Map<String, String> createSuccessResponse(String message) {
        Map<String, String> response = new HashMap<>();
        response.put("message", message);
        return response;
    }
}
