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
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "posts")
public class Post extends TimeStamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String postName;

    @Column(nullable = false, length = 1000)
    private String postContent;

    @Column(nullable = false)
    private String postImage;

    @Column(nullable = false)
    private Long userId;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false, length = 50)
    private PostEnum postType;

    @Enumerated(EnumType.STRING)
    private DeletionStatus deletionStatus = DeletionStatus.N;

    private Post(String postName, String postContent, String postImage, Long userId,
        PostEnum postType) {
        this.postName = postName;
        this.postContent = postContent;
        this.postImage = postImage;
        this.userId = userId;
        this.postType = postType;
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
        this.postName = requestDto.getPostName();
        this.postContent = requestDto.getPostContent();
        this.postImage = requestDto.getPostImage();
    }

    public void softDelete() {
        this.deletionStatus = DeletionStatus.Y;
    }
}
