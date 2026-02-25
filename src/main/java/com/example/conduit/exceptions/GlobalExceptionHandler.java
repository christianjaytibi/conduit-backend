package com.example.conduit.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.ErrorResponse;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.LinkedHashMap;

@RestControllerAdvice
public class GlobalExceptionHandler {

  /**
   * Business-related logic errors
   */
  @ExceptionHandler(ApiException.class)
  public ProblemDetail handleErrorResponse(ErrorResponse ex) {
    return ex.getBody();
  }

  /**
   * Failed validations
   * @return 422 UNPROCESSABLE CONTENT
   */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ProblemDetail handleMethodArgumentNotValidException(
    MethodArgumentNotValidException ex) {
    var errors = ex.getBindingResult()
      .getFieldErrors().stream()
      .map(error  -> {
        var field = new LinkedHashMap<>();
        field.put("field", error.getField());
        field.put("reason", error.getDefaultMessage());
        return field;
      })
      .toList();

    var problem = ProblemDetail.forStatus(HttpStatus.UNPROCESSABLE_CONTENT);
    problem.setDetail("One or more fields are invalid. Please check the errors object.");
    problem.setTitle("Validation failed");
    problem.setProperty("errors", errors);
    return problem;
  }

  /**
   * Invalid JSON or malformed body.
   * @return 400 BAD REQUEST
   */
  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ProblemDetail handleHttpMessageNotReadable() {
    var problem = ProblemDetail.
      forStatusAndDetail(HttpStatus.BAD_REQUEST, "The HTTP body is unreadable or malformed.");
    problem.setTitle("Missing or malformed JSON");
    return problem;
  }

  /**
   * Mistake - HTTP Method
   * @return 405 METHOD NOT ALLOWED
   */
  @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
  public ProblemDetail handleMethodNotSupported(HttpRequestMethodNotSupportedException ex) {
    ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.METHOD_NOT_ALLOWED, ex.getMessage());
    problem.setTitle("HTTP Method Not Allowed");
    return problem;
  }

  /**
   * Mistake - Requesting a URL that doesn't exist.
   * Note: Requires 'spring.mvc.throw-exception-if-no-handler-found=true' in properties.
   */
  @ExceptionHandler(NoHandlerFoundException.class)
  public ProblemDetail handleNoHandlerFound(NoHandlerFoundException ex) {
    String url = ex.getRequestURL();
    ProblemDetail problem = ProblemDetail.forStatusAndDetail(
      HttpStatus.NOT_FOUND,
      "The requested URL %s was not found on this server. That’s all we know.".formatted(url));
    problem.setTitle("Resource Not Found");
    return problem;
  }

  /**
   * Missing - Content-Type: application/json
   * @return 415 UNSUPPORTED MEDIA TYPE
   */
  @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
  public ProblemDetail handleMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex) {
    String supported = ex.getSupportedMediaTypes().toString();
    var problem = ProblemDetail.forStatusAndDetail(
      HttpStatus.UNSUPPORTED_MEDIA_TYPE,
      "We can't read that format. Supported types: " + supported);
    problem.setTitle("Unsupported Media Type");
    return problem;
  }

  /**
   * Missing - Accept: application/json
   * @return 406 NOT ACCEPTABLE
   */
  @ExceptionHandler(HttpMediaTypeNotAcceptableException.class)
  public ProblemDetail handleMediaTypeNotAcceptable() {
    var problem = ProblemDetail.forStatusAndDetail(
      HttpStatus.NOT_ACCEPTABLE,
      "The server cannot produce a response matching the list of acceptable types in the request.");
    problem.setTitle("Not Acceptable");
    return problem;
  }

  /**
   * Generic server-side errors
   * @return 500 INTERNAL SERVER ERROR
   */
  @ExceptionHandler(Exception.class)
  public ProblemDetail handleGenericException() {
    return ProblemDetail.forStatusAndDetail(
      HttpStatus.INTERNAL_SERVER_ERROR,
      "An unexpected error occurred.");
  }
}
