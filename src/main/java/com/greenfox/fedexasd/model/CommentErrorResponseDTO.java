package com.greenfox.fedexasd.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentErrorResponseDTO {
  private String status = "error";
  private String message;

  public CommentErrorResponseDTO(String message) {
    this.message = message;
  }
}
