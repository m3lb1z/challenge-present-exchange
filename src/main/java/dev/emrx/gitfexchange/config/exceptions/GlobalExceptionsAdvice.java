package dev.emrx.gitfexchange.config.exceptions;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionsAdvice {
  
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<List<DataErrorValidation>> handleError400(MethodArgumentNotValidException ex) {
    var errors = ex.getFieldErrors().stream().map(DataErrorValidation::new).toList();
    return ResponseEntity.badRequest().body(errors);
  }

}
