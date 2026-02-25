package com.example.conduit.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.ErrorResponseException;

public class ApiException extends ErrorResponseException {
  public ApiException(String detail, HttpStatus status) {
    super(status);
    super.setDetail(detail);
  }
}
