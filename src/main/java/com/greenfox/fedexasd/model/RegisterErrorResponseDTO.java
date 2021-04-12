package com.greenfox.fedexasd.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class RegisterErrorResponseDTO {
  private String status = "error";
  private String message;

  public RegisterErrorResponseDTO(String message) {
    this.message = message;
  }
}
