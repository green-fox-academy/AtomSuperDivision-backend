package com.greenfox.fedexasd.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoginErrorResponseDTO extends LoginResponseDTO{
  private String message;

  public LoginErrorResponseDTO(String status, String message) {
    super(status);
    this.message = message;
  }

}
