package com.greenfox.fedexasd.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.sql.Timestamp;

@Entity
@Table(name = "comments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Comment {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Type(type = "text")
  private String message;

  @Column(name = "created_at")
  private Timestamp createdAt;

  @ManyToOne
  @JoinColumn(name = "meme_id", nullable = false)
  private Meme meme;

  @ManyToOne
  @JoinColumn(name = "user_id", nullable = false)
  private User user;


  public Comment(String message, User user, Timestamp createdAt, Meme meme) {
    this.message = message;
    this.user = user;
    this.createdAt = createdAt;
    this.meme = meme;
  }
}
