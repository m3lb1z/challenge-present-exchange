package dev.emrx.gitfexchange.users.dto;

import java.util.List;

public record UserResponse(
  Long id,
  String username, 
  String email,
  List<String> roles 
) {
  
}
