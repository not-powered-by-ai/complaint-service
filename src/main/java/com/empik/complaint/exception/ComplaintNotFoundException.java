package com.empik.complaint.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ComplaintNotFoundException extends RuntimeException {

  public ComplaintNotFoundException(long id) {
    super("Complaint " + id + " was not found");
  }
}
