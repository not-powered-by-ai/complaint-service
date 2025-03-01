package com.empik.complaint.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FAILED_DEPENDENCY)
public class WrongResponseStatusException extends RuntimeException {

  public WrongResponseStatusException(int code) {
    super("Unexpected http response status: " + code);
  }
}
