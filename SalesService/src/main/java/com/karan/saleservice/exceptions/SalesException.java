package com.karan.saleservice.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.RestClientException;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
public class SalesException extends Throwable {

  public SalesException(String exception) {
    log.error(exception);
    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, exception);
  }

  public SalesException(HttpStatus httpStatus, String errorMessage, RestClientException e) {
    log.error(errorMessage);
    throw new ResponseStatusException(httpStatus, errorMessage, e);
  }
}