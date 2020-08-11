package com.karan.userservice.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.karan.userservice.exceptions.UserException;
import com.karan.userservice.model.OktaUser;
import com.karan.userservice.model.Profile;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@SpringBootTest
public class UserControllerTest {

  @Value("${okta.SSWS.token}")
  private String tokenValue;

  @Value("${okta.getAllUser.url}")
  private String url;
  @Mock
  RestTemplate mockedRestTemplate;
  @Mock
  WebClient client;
  @InjectMocks
  UserController userController;


  @Test
  public void getUserByIdPositiveTest() throws UserException {
    OktaUser user = new OktaUser();
    HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);
    Principal principal = mock(Principal.class);
    when(httpServletRequest.getUserPrincipal()).thenReturn(principal);
    when(principal.getName()).thenReturn("superAdmin@something.com");
    Profile profile = new Profile();
    profile.setEmail("superAdmin@something.com");
    user.setProfile(profile);
    WebClient.RequestHeadersUriSpec uriSpecMock=mock(WebClient.RequestHeadersUriSpec.class);
    WebClient.RequestHeadersSpec headersSpecMock=mock(WebClient.RequestHeadersSpec.class);
    WebClient.ResponseSpec responseSpecMock=mock(WebClient.ResponseSpec.class);
    when(client.get()).thenReturn(uriSpecMock);
    when(uriSpecMock.uri(ArgumentMatchers.<String>notNull())).thenReturn(headersSpecMock);
    when(headersSpecMock.header(anyString(), anyString())).thenReturn(headersSpecMock);
    when(headersSpecMock.retrieve()).thenReturn(responseSpecMock);
    when(responseSpecMock.bodyToMono(ArgumentMatchers.<Class<OktaUser>>notNull()))
            .thenReturn(Mono.just(user));
    assertEquals(ResponseEntity.ok(user), userController.getUserById("my", httpServletRequest).block());
  }


  @Test
  public void getUserByIdNegativeTest() {
    OktaUser user = new OktaUser();
    HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);
    Principal principal = mock(Principal.class);
    when(httpServletRequest.getUserPrincipal()).thenReturn(principal);
    when(principal.getName()).thenReturn("superAdmin@something.com");
    Profile profile = new Profile();
    profile.setEmail("superAdmin@something.com");
    user.setProfile(profile);
    WebClient.RequestHeadersUriSpec uriSpecMock=mock(WebClient.RequestHeadersUriSpec.class);
    WebClient.RequestHeadersSpec headersSpecMock=mock(WebClient.RequestHeadersSpec.class);
    WebClient.ResponseSpec responseSpecMock=mock(WebClient.ResponseSpec.class);
    when(client.get()).thenReturn(uriSpecMock);
    when(uriSpecMock.uri(ArgumentMatchers.<String>notNull())).thenReturn(headersSpecMock);
    when(headersSpecMock.header(anyString(), anyString())).thenReturn(headersSpecMock);
    when(headersSpecMock.retrieve()).thenReturn(responseSpecMock);
    when(responseSpecMock.bodyToMono(ArgumentMatchers.<Class<OktaUser>>notNull()))
            .thenThrow(HttpClientErrorException.class);

    assertThrows(ResponseStatusException.class, () -> {
      userController.getUserById("my", httpServletRequest);
    });

  }

  @Test
  public void getAllUserPositiveTest() {
    List<OktaUser> users = new ArrayList<>();
    HttpHeaders headers = new HttpHeaders();
    OktaUser user = new OktaUser();
    Profile profile = new Profile();
    profile.setEmail("superAdmin@something.com");
    user.setProfile(profile);
    users.add(user);
    String token = "SSWS " + null;
    headers.set("Authorization", token);
    HttpEntity entity = new HttpEntity(headers);
    WebClient.RequestHeadersUriSpec uriSpecMock=mock(WebClient.RequestHeadersUriSpec.class);
    WebClient.RequestHeadersSpec headersSpecMock=mock(WebClient.RequestHeadersSpec.class);
    WebClient.ResponseSpec responseSpecMock=mock(WebClient.ResponseSpec.class);
    when(client.get()).thenReturn(uriSpecMock);

    when(uriSpecMock.header(anyString(), anyString())).thenReturn(headersSpecMock);
    when(headersSpecMock.retrieve()).thenReturn(responseSpecMock);
    when(responseSpecMock.bodyToFlux(ArgumentMatchers.<Class<OktaUser>>notNull()))
            .thenReturn(Flux.just(user));
    assertEquals(ResponseEntity.ok(users), userController.getAllUsers().block());
  }

  @Test
  public void getAllUserNegativeTest() {
    List<OktaUser> users = new ArrayList<>();
    HttpHeaders headers = new HttpHeaders();
    OktaUser user = new OktaUser();
    Profile profile = new Profile();
    profile.setEmail("superAdmin@something.com");
    user.setProfile(profile);
    String token = "SSWS " + null;
    headers.set("Authorization", token);
    HttpEntity entity = new HttpEntity(headers);
    WebClient.RequestHeadersUriSpec uriSpecMock=mock(WebClient.RequestHeadersUriSpec.class);
    WebClient.RequestHeadersSpec headersSpecMock=mock(WebClient.RequestHeadersSpec.class);
    WebClient.ResponseSpec responseSpecMock=mock(WebClient.ResponseSpec.class);
    when(client.get()).thenReturn(uriSpecMock);

    when(uriSpecMock.header(anyString(), anyString())).thenReturn(headersSpecMock);
    when(headersSpecMock.retrieve()).thenReturn(responseSpecMock);
    when(responseSpecMock.bodyToFlux(ArgumentMatchers.<Class<OktaUser>>notNull()))
            .thenReturn(Flux.empty());
    assertEquals(users, userController.getAllUsers().block().getBody());
  }



}
