package com.popcorntalk.post;

import com.popcorntalk.domain.post.dto.PostCreateRequestDto;
import com.popcorntalk.domain.post.dto.PostGetResponseDto;
import com.popcorntalk.domain.post.dto.PostUpdateRequestDto;
import com.popcorntalk.domain.post.entity.Post;
import com.popcorntalk.domain.post.entity.PostEnum;
import com.popcorntalk.domain.user.entity.User;
import com.popcorntalk.domain.user.entity.UserRoleEnum;
import com.popcorntalk.global.entity.DeletionStatus;
import java.time.LocalDateTime;

public class PostTestData {

    //필드값
    String ANOTHER_PREFIX = "another-";
    String ADMIN_PREFIX = "admin-";
    String UPDATE_PREFIX = "update-";
    String DELETE_PREFIX = "delete-";
    String POST_NAME = "postName1";
    String POST_CONTENT = "postContent1";
    String POST_IMAGE = "htttp://localhost:8080/image.png";
    String EMAIL = "one@one.com";
    LocalDateTime CREATE_AT = LocalDateTime.of(2024, 3, 30, 12, 0, 0);
    LocalDateTime CREATE_AT_PLUS_1HOUR = LocalDateTime.of(2024, 3, 30, 13, 0, 0);
    LocalDateTime MODIFIED_AT = LocalDateTime.now();
    Long TEST_POST_ID = 1L;
    Long TEST_NOTICE_POST_ID = 2L;
    Long TEST_DELETE_POST_ID = 3L;
    Long TEST_USER_ID = 1L;
    Long TEST_ANOTHER_USER_ID = 2L;
    Long TEST_ADMIN_USER_ID = 3L;
    String USER_PASSWORD = "123456789a";
    Long USER_POINT = 5_000L;
    Long ANOTHER_USER_POINT = 3_000L;
    Long MAX_DAILY_POSTS_LIMIT = 3L;

    //조회 DTO
    PostGetResponseDto TEST_GET_RESPONSE_DTO = PostGetResponseDto.builder()
        .postName(POST_NAME)
        .postContent(POST_CONTENT)
        .postImage(POST_IMAGE)
        .email(EMAIL)
        .createdAt(CREATE_AT)
        .modifiedAt(MODIFIED_AT)
        .build();

    PostGetResponseDto TEST_ANOTHER_GET_RESPONSE_DTO = PostGetResponseDto.builder()
        .postName(ANOTHER_PREFIX + POST_NAME)
        .postContent(ANOTHER_PREFIX + POST_CONTENT)
        .postImage(ANOTHER_PREFIX + POST_IMAGE)
        .email(ANOTHER_PREFIX + EMAIL)
        .createdAt(CREATE_AT_PLUS_1HOUR)
        .modifiedAt(MODIFIED_AT)
        .build();

    PostGetResponseDto TEST_GET_DELETE_RESPONSE_DTO = PostGetResponseDto.builder()
        .postName(DELETE_PREFIX + POST_NAME)
        .postContent(DELETE_PREFIX + POST_CONTENT)
        .postImage(DELETE_PREFIX + POST_IMAGE)
        .email(EMAIL)
        .createdAt(CREATE_AT)
        .modifiedAt(MODIFIED_AT)
        .build();


    //생성 DTO
    PostCreateRequestDto TEST_CREATE_REQUEST_DTO = PostCreateRequestDto.builder()
        .postName(POST_NAME)
        .postContent(POST_CONTENT)
        .postImage(POST_IMAGE)
        .build();

    PostCreateRequestDto TEST_NOTICE_CREATE_REQUEST_DTO = PostCreateRequestDto.builder()
        .postName(ADMIN_PREFIX + POST_NAME)
        .postContent(ADMIN_PREFIX + POST_CONTENT)
        .postImage(ADMIN_PREFIX + POST_IMAGE)
        .build();

    //수정 DTO
    PostUpdateRequestDto TEST_UPDATE_REQUEST_DTO = PostUpdateRequestDto.builder()
        .postName(UPDATE_PREFIX + POST_NAME)
        .postContent(UPDATE_PREFIX + POST_CONTENT)
        .postImage(UPDATE_PREFIX + POST_IMAGE)
        .build();

    //User
    User TEST_USER = User.builder()
        .id(TEST_USER_ID)
        .email(EMAIL)
        .password(USER_PASSWORD)
        .role(UserRoleEnum.USER)
        .deletionStatus(DeletionStatus.N)
        .build();

    User TEST_ANTHER_USER = User.builder()
        .id(TEST_ANOTHER_USER_ID)
        .email(ANOTHER_PREFIX + EMAIL)
        .password(ANOTHER_PREFIX + USER_PASSWORD)
        .role(UserRoleEnum.USER)
        .deletionStatus(DeletionStatus.N)
        .build();

    User TEST_ADMIN_USER = User.builder()
        .id(TEST_ADMIN_USER_ID)
        .email(ADMIN_PREFIX + EMAIL)
        .password(ADMIN_PREFIX + USER_PASSWORD)
        .role(UserRoleEnum.ADMIN)
        .deletionStatus(DeletionStatus.N)
        .build();

    //Post
    Post TEST_POST = Post.builder()
        .id(TEST_POST_ID)
        .name(POST_NAME)
        .content(POST_CONTENT)
        .image(POST_IMAGE)
        .userId(TEST_USER_ID)
        .type(PostEnum.POSTED)
        .deletionStatus(DeletionStatus.N)
        .build();

    Post TEST_NOTICE_POST = Post.builder()
        .id(TEST_NOTICE_POST_ID)
        .name(ADMIN_PREFIX + POST_NAME)
        .content(ADMIN_PREFIX + POST_CONTENT)
        .image(ADMIN_PREFIX + POST_IMAGE)
        .userId(TEST_ADMIN_USER_ID)
        .type(PostEnum.NOTICED)
        .deletionStatus(DeletionStatus.N)
        .build();

    Post TEST_DELETE_POST = Post.builder()
        .id(TEST_DELETE_POST_ID)
        .name(POST_NAME)
        .content(POST_CONTENT)
        .image(POST_IMAGE)
        .userId(TEST_USER_ID)
        .type(PostEnum.POSTED)
        .deletionStatus(DeletionStatus.Y)
        .build();
}
