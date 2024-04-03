package com.popcorntalk.domain.comment.repository;

import com.popcorntalk.domain.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long>,
    CommentRepositoryCustom {
}
