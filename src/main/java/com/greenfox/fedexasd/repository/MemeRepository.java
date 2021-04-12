package com.greenfox.fedexasd.repository;

import com.greenfox.fedexasd.model.Meme;
import com.greenfox.fedexasd.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemeRepository extends JpaRepository<Meme, Long> {
}
