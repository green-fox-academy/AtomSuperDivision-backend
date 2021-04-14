package com.greenfox.fedexasd.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MemeMinDTO {
  private Long id;
  private String caption;
  private String url;
  private Long hitCount = 0L;
}
