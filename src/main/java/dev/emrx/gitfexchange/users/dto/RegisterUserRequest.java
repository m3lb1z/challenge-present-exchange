package dev.emrx.gitfexchange.users.dto;

/*

{
  "username": "user",
  "email": "user@springsecurity.dev",
  "password": "user",
  "repassword": "user"
}

 */

public record RegisterUserRequest(String username, String email, String password, String repassword) {
}
