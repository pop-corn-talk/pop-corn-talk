package com.popcorntalk.domain.comment.repository;

import com.popcorntalk.domain.comment.entity.Comment;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long>,
    CommentRepositoryCustom {
    Optional<Comment> findFirstByPostIdOrderByCreatedAtDesc(Long cardId);
}
