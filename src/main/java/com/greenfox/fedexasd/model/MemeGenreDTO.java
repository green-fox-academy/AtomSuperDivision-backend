package com.greenfox.fedexasd.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MemeGenreDTO {
  private String caption;
  private String genre;
  private String url;
  private Long hitCount;
  private Long funny;
  private Long sad;
  private Long erotic;
  private Long scary;
}
