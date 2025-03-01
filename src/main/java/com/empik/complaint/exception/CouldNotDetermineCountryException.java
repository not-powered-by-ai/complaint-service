package com.empik.complaint.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class CouldNotDetermineCountryException extends RuntimeException {
  private static String COULD_NOT_DETERMINE_COUNTRY_FOR = "Could not determine the country for ";
  public CouldNotDetermineCountryException(String ip) {
    super(COULD_NOT_DETERMINE_COUNTRY_FOR + ip);
  }

  public CouldNotDetermineCountryException(String ip, Throwable cause) {
    super(COULD_NOT_DETERMINE_COUNTRY_FOR + ip, cause);
  }
}
