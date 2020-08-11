package com.karan.userservice.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class Email {

  private String value;
  private String status;
  private String type;
}
