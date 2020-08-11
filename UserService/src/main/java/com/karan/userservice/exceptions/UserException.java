package com.karan.userservice.exceptions;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
public class UserException extends Throwable {

  public UserException(String exception) {
    log.error(exception);
    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, exception);
  }

}
