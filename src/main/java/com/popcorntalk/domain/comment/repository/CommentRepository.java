package com.popcorntalk.domain.comment.repository;

import com.popcorntalk.domain.comment.entity.Comment;
import java.time.LocalDateTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long>,
    CommentRepositoryCustom {
    @Query(value = "SELECT COUNT(*) FROM Comment c WHERE c.createdAt BETWEEN :todayStart AND :todayEnd and c.userId = :userId AND c.postId=:postId")
    int getCreateCommentCountInToday(LocalDateTime todayStart,LocalDateTime todayEnd, Long userId,Long postId);
}
