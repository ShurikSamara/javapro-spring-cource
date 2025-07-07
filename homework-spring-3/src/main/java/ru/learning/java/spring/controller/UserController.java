package ru.learning.java.spring.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.learning.java.spring.dto.UserRequest;
import ru.learning.java.spring.model.User;
import ru.learning.java.spring.service.UserService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

  private static final Logger logger = LoggerFactory.getLogger(UserController.class);

  private final UserService userService;

  @Autowired
  public UserController(UserService userService) {
    this.userService = userService;
  }

  @GetMapping("/{id}")
  public ResponseEntity<User> getUserById(@PathVariable("id") Long id) {
    try {
      Optional<User> user = userService.getUserById(id);
      return user.map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
    } catch (IllegalArgumentException e) {
      return ResponseEntity.badRequest().build();
    }
  }

  @GetMapping
  public ResponseEntity<List<User>> getAllUsers() {
    List<User> users = userService.getAllUsers();
    return ResponseEntity.ok(users);
  }

  @PostMapping
  public ResponseEntity<User> createUser(@RequestBody UserRequest userRequest) {
    try {
      User createdUser = userService.createUser(userRequest.getUsername());
      return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    } catch (IllegalArgumentException e) {
      return ResponseEntity.badRequest().build();
    }
  }

  @PutMapping("/{id}")
  public ResponseEntity<User> updateUser(@PathVariable("id") Long id, @RequestBody UserRequest userRequest) {
    try {
      User updatedUser = userService.updateUser(id, userRequest.getUsername());
      return ResponseEntity.ok(updatedUser);
    } catch (IllegalArgumentException e) {
      return ResponseEntity.badRequest().build();
    }
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteUser(@PathVariable("id") Long id) {
    try {
      boolean deleted = userService.deleteUser(id);
      if (deleted) {
        return ResponseEntity.noContent().build();
      } else {
        return ResponseEntity.notFound().build();
      }
    } catch (IllegalArgumentException e) {
      logger.warn("Неверный запрос при удалении пользователя: {}", e.getMessage());
      return ResponseEntity.badRequest().build();
    } catch (IllegalStateException e) {
      logger.warn("Конфликт при удалении пользователя: {}", e.getMessage());
      return ResponseEntity.status(HttpStatus.CONFLICT).build();
    } catch (RuntimeException e) {
      logger.error("Внутренняя ошибка при удалении пользователя с ID {}: {}", id, e.getMessage());
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

  }

  @GetMapping("/search")
  public ResponseEntity<User> findByUsername(@RequestParam String username) {
    if (username == null || username.trim().isEmpty()) {
      return ResponseEntity.badRequest().build();
    }
    Optional<User> user = userService.findByUsername(username);
    return user.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
  }

  @GetMapping("/search/containing")
  public ResponseEntity<List<User>> findUsersContaining(@RequestParam String pattern) {
    List<User> users = userService.findUsersContaining(pattern);
    return ResponseEntity.ok(users);
  }

  @GetMapping("/count")
  public ResponseEntity<Long> getUserCount() {
    long count = userService.getUserCount();
    return ResponseEntity.ok(count);
  }
}