package com.popcorntalk.domain.comment.entity;

import com.popcorntalk.domain.comment.dto.CommentUpdateRequestDto;
import com.popcorntalk.global.entity.DeletionStatus;
import com.popcorntalk.global.entity.TimeStamped;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "comments")
public class Comment extends TimeStamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 500)
    private String content;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Long postId;

    @Enumerated(value = EnumType.STRING)
    private DeletionStatus deletionStatus;

    private Comment(String comment, Long userId, Long postId) {
        this.content = comment;
        this.userId = userId;
        this.postId = postId;
        this.deletionStatus = DeletionStatus.N;
    }

    public static Comment createOf(String content, Long userId, Long postId) {
        return new Comment(content, userId, postId);
    }

    public void update(CommentUpdateRequestDto requestDto) {
        this.content = requestDto.getContent();
    }

    public void softDelete() {
        this.deletionStatus = DeletionStatus.Y;
    }
}
