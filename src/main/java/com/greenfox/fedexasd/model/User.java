package com.greenfox.fedexasd.model;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

  @Id
  @Column(unique = true)
  private String username;
  private String password;

  @OneToMany(mappedBy = "user")
  private List<Meme> memes = new ArrayList<>();

  @OneToMany(mappedBy = "user")
  private List<Comment> commentList = new ArrayList<>();

  public User(String username, @NotNull String password) {
    this.username = username;
    this.password = password;
  }
}
