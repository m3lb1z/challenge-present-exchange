package dev.emrx.gitfexchange.users.dto;

import java.util.List;

public record LoginResponse(String username, List<String> roles, String token) {
}
