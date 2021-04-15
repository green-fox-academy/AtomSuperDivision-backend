package com.greenfox.fedexasd.repository;

import com.greenfox.fedexasd.model.Meme;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemeRepository extends JpaRepository<Meme, Long> {
  List<Meme> findAllByGenre(String genre);
}
