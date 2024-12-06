package dev.emrx.gitfexchange.config.exceptions;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.validation.ValidationException;

@RestControllerAdvice
public class GlobalExceptionsAdvice {
  
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<List<FieldErrorResponse>> handleError400(MethodArgumentNotValidException ex) {
    var errors = ex.getFieldErrors().stream().map(FieldErrorResponse::new).toList();
    return ResponseEntity.badRequest().body(errors);
  }

  @ExceptionHandler(ValidationException.class)
  public ResponseEntity<String> handleValidationException(ValidationException ex) {
      return ResponseEntity.badRequest().body(ex.getMessage());
  }

}
