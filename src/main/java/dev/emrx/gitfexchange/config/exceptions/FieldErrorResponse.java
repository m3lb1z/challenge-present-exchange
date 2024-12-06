package dev.emrx.gitfexchange.config.exceptions;

import org.springframework.validation.FieldError;

public record FieldErrorResponse(String campo, String error) {

  public FieldErrorResponse(FieldError error) {
      this(error.getField(), error.getDefaultMessage());
  }

}
