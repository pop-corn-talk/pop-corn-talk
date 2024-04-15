package com.popcorntalk.mockData;

import com.popcorntalk.domain.point.entity.Point;
import com.popcorntalk.domain.post.dto.PostCreateRequestDto;
import com.popcorntalk.domain.post.dto.PostGetResponseDto;
import com.popcorntalk.domain.post.dto.PostUpdateRequestDto;
import com.popcorntalk.domain.post.entity.Post;
import com.popcorntalk.domain.post.entity.PostEnum;
import com.popcorntalk.domain.user.entity.User;
import com.popcorntalk.domain.user.entity.UserRoleEnum;
import com.popcorntalk.global.entity.DeletionStatus;
import java.time.LocalDateTime;

public class MockData {

    //필드값
    public String ANOTHER_PREFIX = "another-";
    public String ADMIN_PREFIX = "admin-";
    public String UPDATE_PREFIX = "update-";
    public String DELETE_PREFIX = "delete-";
    public String POST_NAME = "postName1";
    public String POST_CONTENT = "postContent1";
    public String POST_IMAGE = "htttp://localhost:8080/image.png";
    public String EMAIL = "one@one.com";
    public LocalDateTime CREATE_AT = LocalDateTime.of(2024, 3, 30, 12, 0, 0);
    public LocalDateTime CREATE_AT_PLUS_1HOUR = LocalDateTime.of(2024, 3, 30, 13, 0, 0);
    public LocalDateTime MODIFIED_AT = LocalDateTime.now();
    public Long TEST_POST_ID = 1L;
    public Long TEST_NOTICE_POST_ID = 2L;
    public Long TEST_DELETE_POST_ID = 3L;
    public Long TEST_USER_ID = 1L;
    public Long TEST_ANOTHER_USER_ID = 2L;
    public Long TEST_ADMIN_USER_ID = 3L;
    public String USER_PASSWORD = "123456789a";
    public int USER_POINT = 5000;
    public Long ANOTHER_USER_POINT = 3_000L;
    public Long MAX_DAILY_POSTS_LIMIT = 3L;
    public Long TEST_POINT_ID = 1L;

    //조회 DTO
    public PostGetResponseDto TEST_GET_RESPONSE_DTO = PostGetResponseDto.builder()
        .name(POST_NAME)
        .content(POST_CONTENT)
        .image(POST_IMAGE)
        .email(EMAIL)
        .createdAt(CREATE_AT)
        .modifiedAt(MODIFIED_AT)
        .build();

    public PostGetResponseDto TEST_ANOTHER_GET_RESPONSE_DTO = PostGetResponseDto.builder()
        .name(ANOTHER_PREFIX + POST_NAME)
        .content(ANOTHER_PREFIX + POST_CONTENT)
        .image(ANOTHER_PREFIX + POST_IMAGE)
        .email(ANOTHER_PREFIX + EMAIL)
        .createdAt(CREATE_AT_PLUS_1HOUR)
        .modifiedAt(MODIFIED_AT)
        .build();

    public PostGetResponseDto TEST_GET_DELETE_RESPONSE_DTO = PostGetResponseDto.builder()
        .name(DELETE_PREFIX + POST_NAME)
        .content(DELETE_PREFIX + POST_CONTENT)
        .image(DELETE_PREFIX + POST_IMAGE)
        .email(EMAIL)
        .createdAt(CREATE_AT)
        .modifiedAt(MODIFIED_AT)
        .build();


    //생성 DTO
    public PostCreateRequestDto TEST_CREATE_REQUEST_DTO = PostCreateRequestDto.builder()
        .postName(POST_NAME)
        .postContent(POST_CONTENT)
        .postImage(POST_IMAGE)
        .build();

    public PostCreateRequestDto TEST_NOTICE_CREATE_REQUEST_DTO = PostCreateRequestDto.builder()
        .postName(ADMIN_PREFIX + POST_NAME)
        .postContent(ADMIN_PREFIX + POST_CONTENT)
        .postImage(ADMIN_PREFIX + POST_IMAGE)
        .build();

    //수정 DTO
    public PostUpdateRequestDto TEST_UPDATE_REQUEST_DTO = PostUpdateRequestDto.builder()
        .postName(UPDATE_PREFIX + POST_NAME)
        .postContent(UPDATE_PREFIX + POST_CONTENT)
        .postImage(UPDATE_PREFIX + POST_IMAGE)
        .build();

    //User
    public User TEST_USER = User.builder()
        .id(TEST_USER_ID)
        .email(EMAIL)
        .password(USER_PASSWORD)
        .role(UserRoleEnum.USER)
        .deletionStatus(DeletionStatus.N)
        .build();

    public User TEST_ANTHER_USER = User.builder()
        .id(TEST_ANOTHER_USER_ID)
        .email(ANOTHER_PREFIX + EMAIL)
        .password(ANOTHER_PREFIX + USER_PASSWORD)
        .role(UserRoleEnum.USER)
        .deletionStatus(DeletionStatus.N)
        .build();

    public User TEST_ADMIN_USER = User.builder()
        .id(TEST_ADMIN_USER_ID)
        .email(ADMIN_PREFIX + EMAIL)
        .password(ADMIN_PREFIX + USER_PASSWORD)
        .role(UserRoleEnum.ADMIN)
        .deletionStatus(DeletionStatus.N)
        .build();

    //Post
    public Post TEST_POST = Post.builder()
        .id(TEST_POST_ID)
        .name(POST_NAME)
        .content(POST_CONTENT)
        .image(POST_IMAGE)
        .userId(TEST_USER_ID)
        .type(PostEnum.POSTED)
        .deletionStatus(DeletionStatus.N)
        .build();

    public Post TEST_NOTICE_POST = Post.builder()
        .id(TEST_NOTICE_POST_ID)
        .name(ADMIN_PREFIX + POST_NAME)
        .content(ADMIN_PREFIX + POST_CONTENT)
        .image(ADMIN_PREFIX + POST_IMAGE)
        .userId(TEST_ADMIN_USER_ID)
        .type(PostEnum.NOTICED)
        .deletionStatus(DeletionStatus.N)
        .build();

    public Post TEST_DELETE_POST = Post.builder()
        .id(TEST_DELETE_POST_ID)
        .name(POST_NAME)
        .content(POST_CONTENT)
        .image(POST_IMAGE)
        .userId(TEST_USER_ID)
        .type(PostEnum.POSTED)
        .deletionStatus(DeletionStatus.Y)
        .build();

    public Point TEST_POINT = Point.builder()
        .id(TEST_POINT_ID)
        .userId(TEST_USER_ID)
        .point(USER_POINT)
        .build();
}

