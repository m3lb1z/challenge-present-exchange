package dev.emrx.gitfexchange.users.listener;

import org.springframework.security.crypto.password.PasswordEncoder;

import dev.emrx.gitfexchange.users.model.User;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

public class UserEntityListener {
  
  private final PasswordEncoder passwordEncoder;

  public UserEntityListener(PasswordEncoder passwordEncoder) {
    this.passwordEncoder = passwordEncoder;
  }

  @PrePersist
  @PreUpdate
  public void encryptPassword(User user) {
    user.setPassword(passwordEncoder.encode(user.getPassword()));
  }
}
