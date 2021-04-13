package com.greenfox.fedexasd.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MemeDTO {
  private String caption;
  private String url;
  private Long funny = 0L;
  private Long sad = 0L;
  private Long erotic = 0L;
  private Long scary = 0L;
  private Timestamp createdAt;
  private String username;
}
