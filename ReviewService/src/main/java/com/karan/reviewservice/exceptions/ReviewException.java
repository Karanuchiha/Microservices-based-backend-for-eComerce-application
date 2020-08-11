package com.karan.reviewservice.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.RestClientException;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
public class ReviewException extends Throwable {

  public ReviewException(String exception) {
    log.error(exception);
    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, exception);
  }

  public ReviewException(HttpStatus httpStatus, String errorMessage, RestClientException e) {
    log.error(errorMessage);
    throw new ResponseStatusException(httpStatus, errorMessage, e);
  }
}
