package dev.emrx.gitfexchange.participants.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateParticipantRequest(
    @NotBlank(message = "Name cannot be blank")
    String name, 

    @NotBlank(message = "Email cannot be blank")
    String email) {
  
}