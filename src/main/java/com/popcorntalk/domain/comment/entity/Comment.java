package com.popcorntalk.domain.comment.entity;

import com.popcorntalk.domain.comment.dto.CommentCreateRequestDto;
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
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
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

    @Builder
    public Comment(Long id, String comment, Long userId, Long postId,
        DeletionStatus deletionStatus) {
        this.id = id;
        this.content = comment;
        this.userId = userId;
        this.postId = postId;
        this.deletionStatus = DeletionStatus.N;
    }

    public static Comment createOf(CommentCreateRequestDto requestDto, Long userId, Long postId) {
        return Comment.builder()
            .comment(requestDto.getContent())
            .userId(userId)
            .postId(postId)
            .build();
    }

    public void update(CommentUpdateRequestDto requestDto) {
        this.content = requestDto.getContent();
    }

    public void softDelete() {
        this.deletionStatus = DeletionStatus.Y;
    }

}
