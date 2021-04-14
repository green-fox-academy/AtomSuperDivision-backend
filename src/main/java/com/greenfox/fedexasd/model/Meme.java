package com.greenfox.fedexasd.model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "memes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Meme {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String caption;
  @Lob
  @Column(columnDefinition = "MEDIUMBLOB")
  private String image;
  private Long funny = 0L;
  private Long sad = 0L;
  private Long erotic = 0L;
  private Long scary = 0L;
  private String genre;

  @Column(name = "hit_count")
  private Long hitCount = 0L;

  @Column(name = "created_at")
  private Timestamp createdAt;

  @ManyToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @OneToMany(mappedBy = "meme", cascade = CascadeType.ALL)
  private List<Comment> commentList = new ArrayList<>();
}
