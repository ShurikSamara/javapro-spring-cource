package ru.learning.java.spring.dto;

import jakarta.validation.constraints.NotBlank;

//Попробую проверить валидацию поля (! если выйдет тяжеловесно - удалить)
public record UserRequest (@NotBlank(message = "Username cannot be blank") String username) {
  public String getUsername() {
    return username;
  }
}