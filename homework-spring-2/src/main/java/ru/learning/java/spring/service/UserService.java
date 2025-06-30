package ru.learning.java.spring.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.learning.java.spring.model.User;
import ru.learning.java.spring.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserService {

  private final UserRepository userRepository;

  @Autowired
  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public User createUser(String username) {
    User user = new User(username);
    return userRepository.save(user);
  }

  @Transactional(readOnly = true)
  public Optional<User> getUserById(Long id) {
    return userRepository.findById(id);
  }

  @Transactional(readOnly = true)
  public List<User> getAllUsers() {
    return userRepository.findAll();
  }

  public User updateUser(Long id, String newUsername) {
    User user = userRepository.findById(id)
      .orElseThrow(() -> new RuntimeException("Пользователь не найден с ID: " + id));
    user.setUsername(newUsername);
    return userRepository.save(user);
  }

  public boolean deleteUser(Long id) {
    if (userRepository.existsById(id)) {
      userRepository.deleteById(id);
      return true;
    }
    return false;
  }

  @Transactional(readOnly = true)
  public Optional<User> findByUsername(String username) {
    return userRepository.findByUsername(username);
  }

  @Transactional(readOnly = true)
  public List<User> findUsersContaining(String pattern) {
    return userRepository.findByUsernameContaining(pattern);
  }

  @Transactional(readOnly = true)
  public long getUserCount() {
    return userRepository.countAllUsers();
  }
}