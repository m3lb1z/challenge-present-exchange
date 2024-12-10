package dev.emrx.gitfexchange.users.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
  @NotBlank(message = "Username no puede estar en blanco")
  String username,
  @NotBlank(message = "Password no puede estar en blanco") 
  String password) {
}
