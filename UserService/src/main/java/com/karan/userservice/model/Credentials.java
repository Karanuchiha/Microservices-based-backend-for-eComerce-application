package com.karan.userservice.model;

import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class Credentials {

  private Password password;
  private List<Email> emails;
  private Recovery_question recovery_question;
  private Provider provider;


}
