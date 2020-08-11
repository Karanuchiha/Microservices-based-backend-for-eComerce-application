package com.karan.userservice.controllers;

import com.karan.userservice.exceptions.UserException;
import com.karan.userservice.model.OktaUser;
import java.security.Principal;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;


@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {

  @Value("${okta.SSWS.token}")
  private String tokenValue;

  @Value("${okta.getAllUser.url}")
  private String url;


  @Autowired
  private RestTemplate restTemplate;
  @Autowired
  private WebClient client;


  @GetMapping(value = "")
  @PreAuthorize("hasAuthority('Admin')")
  public Mono<ResponseEntity<List<OktaUser>>> getAllUsers() {

    String token = "SSWS " + tokenValue;
    Mono<List<OktaUser>> users=client.get().header("Authorization",token).retrieve().bodyToFlux(OktaUser.class).collectList();
    return users.map(oktaUsers -> ResponseEntity.status(HttpStatus.OK).body(oktaUsers))
            .defaultIfEmpty(ResponseEntity.badRequest().build());
  }

  @GetMapping(value = "/{id}")
  @PreAuthorize("hasAuthority('Everyone')")
  public Mono<ResponseEntity<OktaUser>> getUserById(@PathVariable("id") String id, HttpServletRequest request)
      throws UserException {

    Principal principal = request.getUserPrincipal();
    String token = "SSWS " + tokenValue;
    try {
      OktaUser user = client.get().uri("/"+id).header("Authorization",token).retrieve().bodyToMono(OktaUser.class).block();
      if (user.getProfile().getEmail().equals(principal.getName())) {
        return Mono.just(ResponseEntity.ok(user));
      } else {
        throw new UserException(
            String.format("The  token belongs to %s and belongs to %s ",principal.getName(),user.getProfile().getEmail()));
      }
    } catch (HttpClientErrorException exception) {
      throw new UserException(
          String.format("There was error in getting the user %s ", exception.getMessage()));
    }
  }


}


