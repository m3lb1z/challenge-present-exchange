package dev.emrx.gitfexchange.users.dto;


public record RegisterUserRequest(String username, String email, String password, String repassword) {
}
