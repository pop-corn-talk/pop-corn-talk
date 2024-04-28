package com.popcorntalk.domain.post.entity;

import com.popcorntalk.domain.post.dto.PostUpdateRequestDto;
import com.popcorntalk.global.entity.DeletionStatus;
import com.popcorntalk.global.entity.TimeStamped;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "posts", indexes = {
    @Index(name = "idx_createdAt",columnList = "created_at"),
    @Index(name = "idx_name", columnList = "name")})
public class Post extends TimeStamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false, length = 1000)
    private String content;

    @Column(nullable = false)
    private String image;

    @Column(nullable = false)
    private Long userId;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false, length = 50)
    private PostEnum type;

    @Enumerated(EnumType.STRING)
    private DeletionStatus deletionStatus;

    private Post(String name, String content, String image, Long userId, PostEnum type) {
        this.name = name;
        this.content = content;
        this.image = image;
        this.userId = userId;
        this.type = type;
        this.deletionStatus = DeletionStatus.N;
    }

    public static Post createOf(String postName, String postContent, String postImage,
        Long userId) {
        PostEnum postEnum = PostEnum.POSTED;
        return new Post(postName, postContent, postImage, userId, postEnum);
    }

    public static Post noticeOf(String postName, String postContent, String postImage,
        Long userId) {
        PostEnum postEnum = PostEnum.NOTICED;
        return new Post(postName, postContent, postImage, userId, postEnum);
    }

    public void update(PostUpdateRequestDto requestDto) {
        this.name = requestDto.getPostName();
        this.content = requestDto.getPostContent();
        this.image = requestDto.getPostImage();
    }

    public void softDelete() {
        this.deletionStatus = DeletionStatus.Y;
    }
}
