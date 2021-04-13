package com.greenfox.fedexasd.repository;

import com.greenfox.fedexasd.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
