package ru.learning.java.spring.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.learning.java.spring.model.User;
import ru.learning.java.spring.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserService {

  private static final Logger logger = LoggerFactory.getLogger(UserService.class);

  private final UserRepository userRepository;

  @Autowired
  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public User createUser(String username) {
    if (username == null || username.trim().isEmpty()) {
      throw new IllegalArgumentException("Имя пользователя не может быть пустым");
    }
    username = username.trim();

    try {
      User user = new User(username);
      return userRepository.save(user);
    } catch (DataIntegrityViolationException e) {
      throw new RuntimeException("Пользователь с таким именем уже существует: " + username);
    }
  }

  @Transactional(readOnly = true)
  public Optional<User> getUserById(Long id) {
    return userRepository.findById(id);
  }

  @Transactional(readOnly = true)
  public List<User> getAllUsers() {
    return userRepository.findAll();
  }

  @Transactional
  public User updateUser(Long id, String newUsername) {
    if (id == null) {
      throw new IllegalArgumentException("ID пользователя не может быть null");
    }
    if (newUsername == null || newUsername.trim().isEmpty()) {
      throw new IllegalArgumentException("Имя пользователя не может быть пустым");
    }

    Optional<User> existingUser = userRepository.findByUsername(newUsername.trim());
    if (existingUser.isPresent() && !existingUser.get().getId().equals(id)) {
      throw new RuntimeException("Пользователь с именем '" + newUsername + "' уже существует");
    }

    User user = userRepository.findById(id)
      .orElseThrow(() -> new RuntimeException("Пользователь не найден с ID: " + id));

    user.setUsername(newUsername.trim());
    return userRepository.save(user);
  }

  public boolean deleteUser(Long id) {
    if (id == null) {
      throw new IllegalArgumentException("ID пользователя не может быть null");
    }

    try {
      if (!userRepository.existsById(id)) {
        logger.debug("Попытка удалить несуществующего пользователя с ID: {}", id);
        return false;
      }

      userRepository.deleteById(id);
      logger.info("Пользователь с ID {} успешно удален", id);
      return true;

    } catch (DataIntegrityViolationException e) {
      logger.error("Не удалось удалить пользователя с ID {} из-за нарушения ограничений БД: {}", id, e.getMessage());
      throw new IllegalStateException("Невозможно удалить пользователя: на него ссылаются другие данные", e);

    } catch (DataAccessException e) {
      logger.error("Ошибка доступа к базе данных при удалении пользователя с ID {}: {}", id, e.getMessage());
      throw new RuntimeException("Ошибка базы данных при удалении пользователя", e);
    }
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
    return userRepository.count();
  }
}