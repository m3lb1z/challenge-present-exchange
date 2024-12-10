package dev.emrx.gitfexchange.users.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record RegisterUserRequest(
  @NotBlank(message = "Username no puede estar en blanco")
  String username, 
  @NotBlank(message = "Email no puede estar en blanco")
  @Email(message = "Email debe ser válido")
  String email, 
  @NotBlank(message = "Password no puede estar en blanco")
  String password, 
  @NotBlank(message = "Repassword no puede estar en blanco")
  String repassword) {
}
