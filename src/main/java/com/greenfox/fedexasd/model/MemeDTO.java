package com.greenfox.fedexasd.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MemeDTO {
  private Long id;
  private String caption;
  private String image;
  private Long funny = 0L;
  private Long sad = 0L;
  private Long erotic = 0L;
  private Long scary = 0L;
  private Timestamp createdAt;
  private String username;
  private List<CommentSuccessResponseDTO> comments;

  public MemeDTO(Long id, String caption, String image, Long funny, Long sad, Long erotic,
                 Long scary, Timestamp createdAt,
                 String username) {
    this.id = id;
    this.caption = caption;
    this.image = image;
    this.funny = funny;
    this.sad = sad;
    this.erotic = erotic;
    this.scary = scary;
    this.createdAt = createdAt;
    this.username = username;
  }
}
