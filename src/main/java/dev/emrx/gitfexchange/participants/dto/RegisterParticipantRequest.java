package dev.emrx.gitfexchange.participants.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record RegisterParticipantRequest(
    @NotBlank(message = "Name no puede estar en blanco")
    String name, 

    @NotBlank(message = "Email no puede estar en blanco")
    @Email(message = "Email debe ser válido")
    String email) {
  
}
