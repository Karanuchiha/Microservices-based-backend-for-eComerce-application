package com.karan.userservice.model;

import java.util.Date;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class OktaUser {

  private String id;
  private String status;
  private Date created;
  private String activated;
  private Date statusChanged;
  private Date lastLogin;
  private Date passwordChanged;
  private Profile profile;
  private Credentials credentials;
  private Links _links;

}
