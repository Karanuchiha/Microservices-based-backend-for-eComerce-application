package com.karan.userservice.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class Profile {

  private String firstName;
  private String lastName;
  private String mobilePhone;
  private String secondEmail;
  private String login;
  private String email;
}
