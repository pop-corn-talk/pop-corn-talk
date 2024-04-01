package com.popcorntalk.post;

import static com.popcorntalk.global.exception.ErrorCode.PERMISSION_DENIED;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.popcorntalk.domain.point.repository.PointRepository;
import com.popcorntalk.domain.post.dto.PostGetResponseDto;
import com.popcorntalk.domain.post.entity.Post;
import com.popcorntalk.domain.post.repository.PostRepository;
import com.popcorntalk.domain.post.service.PostServiceImpl;
import com.popcorntalk.domain.user.repository.UserRepository;
import com.popcorntalk.global.entity.DeletionStatus;
import com.popcorntalk.global.exception.customException.PermissionDeniedException;
import com.querydsl.core.types.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.test.context.ActiveProfiles;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class PostServiceImplTest extends PostTestData {

    @Mock
    PostRepository postRepository;
    @Mock
    UserRepository userRepository;
    @Mock
    PointRepository pointRepository;
    @InjectMocks
    PostServiceImpl postService;

    @Test
    @DisplayName("Post 단일조회 - 성공")
    void testGetPostSuccess() {
        //given
        given(postRepository.findPost(any(Long.class))).willReturn(TEST_GET_RESPONSE_DTO);

        //when
        PostGetResponseDto response = postRepository.findPost(TEST_POST_ID);

        //then
        assertEquals(TEST_GET_RESPONSE_DTO.getPostName(), response.getPostName());
    }

    @Test
    @DisplayName("Post 단일조회 - 게시물없음")
    void testGetPostNoPost() {
        //given
        given(postRepository.findPost(any(Long.class))).willThrow(
            new IllegalArgumentException("해당하는 게시물이 없습니다."));

        //when
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
            postRepository.findPost(TEST_POST_ID));

        //then
        assertEquals("해당하는 게시물이 없습니다.", exception.getMessage());
    }

    @Test
    @DisplayName("Post 전체조회 - 성공")
    void testGetPostsSuccess() {
        //given
        PageRequest pageRequest = PageRequest.of(0, 3, Direction.ASC, "createdAT");
        boolean hasNext = false;

        List<PostGetResponseDto> TEST_GET_RESPONSE_DTO_LIST = new ArrayList<>();
        TEST_GET_RESPONSE_DTO_LIST.add(TEST_GET_RESPONSE_DTO);
        TEST_GET_RESPONSE_DTO_LIST.add(TEST_ANOTHER_GET_RESPONSE_DTO);
        Slice<PostGetResponseDto> responsesSlice = new SliceImpl<>(TEST_GET_RESPONSE_DTO_LIST,
            pageRequest, hasNext);

        given(postRepository.findPosts(any(Pageable.class), any(Predicate.class))).willReturn(
            responsesSlice);

        //when
        Slice<PostGetResponseDto> returnResponses = postService.getPosts(pageRequest);

        //then
        assertEquals(responsesSlice.get().count(), returnResponses.get().count());
        assertEquals(responsesSlice.getContent().get(0), returnResponses.getContent().get(0));
        assertEquals(responsesSlice.getContent().get(1), returnResponses.getContent().get(1));
    }

    @Test
    @DisplayName("Post 전체조회 - 게시물 없음")
    void testGetPostsNoPosts() {
        //given
        PageRequest pageRequest = PageRequest.of(0, 3, Direction.ASC, "createdAT");

        given(postRepository.findPosts(any(Pageable.class), any(Predicate.class)))
            .willThrow(new IllegalArgumentException("해당하는 게시물이 없습니다."));

        //when
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
            postService.getPosts(pageRequest));

        //then
        assertEquals("해당하는 게시물이 없습니다.", exception.getMessage());
    }

    @Test
    @DisplayName("삭제된 Post 전체조회 - 성공")
    void testGetDeletePostsSuccess() {
        //given
        PageRequest pageRequest = PageRequest.of(0, 3, Direction.ASC, "createdAT");
        boolean hasNext = false;

        List<PostGetResponseDto> TEST_DELETED_GET_RESPONSE_DTO_LIST = new ArrayList<>();
        TEST_DELETED_GET_RESPONSE_DTO_LIST.add(TEST_GET_DELETE_RESPONSE_DTO);
        Slice<PostGetResponseDto> responsesSlice = new SliceImpl<>(
            TEST_DELETED_GET_RESPONSE_DTO_LIST,
            pageRequest, hasNext);

        given(userRepository.findById(any(Long.class))).willReturn(
            Optional.ofNullable(TEST_ADMIN_USER));
        given(postRepository.findPosts(any(Pageable.class), any(Predicate.class))).willReturn(
            responsesSlice);

        //when
        Slice<PostGetResponseDto> returnResponses = postService.getDeletePosts(TEST_ADMIN_USER,
            pageRequest);

        //then
        assertEquals(responsesSlice.get().count(), returnResponses.get().count());
        assertEquals(responsesSlice.getContent().get(0), returnResponses.getContent().get(0));
    }

    @Test
    @DisplayName("삭제된 Post 전체조회 - 게시물 없음")
    void testGetDeletePostsNoPosts() {
        //given
        PageRequest pageRequest = PageRequest.of(0, 3, Direction.ASC, "createdAT");

        given(userRepository.findById(any(Long.class))).willReturn(
            Optional.ofNullable(TEST_ADMIN_USER));
        given(postRepository.findPosts(any(Pageable.class), any(Predicate.class)))
            .willThrow(new IllegalArgumentException("해당하는 게시물이 없습니다."));

        //when
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
            postService.getDeletePosts(TEST_ADMIN_USER, pageRequest));

        //then
        assertEquals("해당하는 게시물이 없습니다.", exception.getMessage());
    }

    @Test
    @DisplayName("삭제된 Post 전체조회 - 권한이 없음")
    void testGetDeletePostsNoPermission() {
        //given
        PageRequest pageRequest = PageRequest.of(0, 3, Direction.ASC, "createdAT");

        given(userRepository.findById(any(Long.class))).willReturn(Optional.ofNullable(TEST_USER));

        //when
        PermissionDeniedException exception = assertThrows(PermissionDeniedException.class, () ->
            postService.getDeletePosts(TEST_ADMIN_USER, pageRequest));

        //then
        assertEquals(PERMISSION_DENIED, exception.getErrorCode());
    }

    @Test
    @Disabled
    @DisplayName("Post 생성 - 성공")
    void testCreatePostSuccess() {
        //given
        given(postRepository.save(any(Post.class))).willReturn(TEST_POST);
        //Point 빌더생기면 추가해야함
//        given(pointRepository.findByUserId(any(Long.class))).willReturn(TEST_POINT)

        //when
        postService.createPost(TEST_USER, TEST_CREATE_REQUEST_DTO);

        //then
        verify(postRepository, times(1)).save(any(Post.class));
    }

    @Test
    @DisplayName("NoticePost 생성 - 성공")
    void testCreateNoticePostSuccess() {
        //given
        given(userRepository.findById(any(Long.class))).willReturn(
            Optional.ofNullable(TEST_ADMIN_USER));
        given(postRepository.save(any(Post.class))).willReturn(TEST_NOTICE_POST);

        //when
        postService.createNoticePost(TEST_ADMIN_USER, TEST_NOTICE_CREATE_REQUEST_DTO);

        //then
        verify(postRepository, times(1)).save(any(Post.class));
    }

    @Test
    @DisplayName("NoticePost 생성 - 일반유저권한")
    void testCreateNoticePostNormalUser() {
        //given
        given(userRepository.findById(any(Long.class))).willReturn(Optional.ofNullable(TEST_USER));

        //when
        PermissionDeniedException exception = assertThrows(PermissionDeniedException.class, () ->
            postService.createNoticePost(TEST_USER, TEST_NOTICE_CREATE_REQUEST_DTO));

        //then
        assertEquals(PERMISSION_DENIED, exception.getErrorCode());
    }

    @Test
    @DisplayName("Post 수정 - 성공")
    void testUpdatePostSuccess() {
        //given
        given(postRepository.findById(any(Long.class))).willReturn(Optional.ofNullable(TEST_POST));
        TEST_POST.update(TEST_UPDATE_REQUEST_DTO);

        //when
        postService.updatePost(TEST_USER, TEST_UPDATE_REQUEST_DTO, TEST_POST_ID);

        //then
        assertEquals(TEST_UPDATE_REQUEST_DTO.getPostName(), TEST_POST.getName());
        assertEquals(TEST_UPDATE_REQUEST_DTO.getPostContent(), TEST_POST.getContent());
        assertEquals(TEST_UPDATE_REQUEST_DTO.getPostImage(), TEST_POST.getImage());
    }

    @Test
    @DisplayName("Post 수정 - 게시물 없음")
    void testUpdatePostNoPost() {
        //given
        given(postRepository.findById(any(Long.class))).willThrow(
            new IllegalArgumentException("해당하는 게시물이 없습니다."));

        //when
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
            postService.updatePost(TEST_USER, TEST_UPDATE_REQUEST_DTO, TEST_POST_ID));

        //then
        assertEquals("해당하는 게시물이 없습니다.", exception.getMessage());
    }

    @Test
    @DisplayName("Post 수정 - 삭제된 게시물")
    void testUpdatePostDeletePost() {
        //given
        given(postRepository.findById(any(Long.class))).willReturn(
            Optional.ofNullable(TEST_DELETE_POST));


        //when
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
            postService.updatePost(TEST_USER, TEST_UPDATE_REQUEST_DTO, TEST_DELETE_POST_ID));

        //then
        assertEquals("삭제된 게시물 입니다.", exception.getMessage());
    }

    @Test
    @DisplayName("Post 수정 - 타인 게시물")
    void testUpdatePostNoPermission() {
        //given
        given(postRepository.findById(any(Long.class))).willReturn(Optional.ofNullable(TEST_POST));


        //when
        PermissionDeniedException exception = assertThrows(PermissionDeniedException.class, () ->
            postService.updatePost(TEST_ANTHER_USER, TEST_UPDATE_REQUEST_DTO, TEST_POST_ID));

        //then
        assertEquals(PERMISSION_DENIED, exception.getErrorCode());
    }

    @Test
    @DisplayName("Post 삭제 - 성공")
    void testDeletePostSuccess() {
        //given
        given(postRepository.findById(any(Long.class))).willReturn(Optional.ofNullable(TEST_POST));

        //when
        postService.deletePost(TEST_USER, TEST_POST_ID);

        //then
        assertEquals(DeletionStatus.Y, TEST_POST.getDeletionStatus());
    }

    @Test
    @DisplayName("Post 삭제 - 게시물 없음")
    void testDeletePostNoPost() {
        //given
        given(postRepository.findById(any(Long.class))).willThrow(
            new IllegalArgumentException("해당하는 게시물이 없습니다."));

        //when
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
            postService.deletePost(TEST_USER, TEST_POST_ID));

        //then
        assertEquals("해당하는 게시물이 없습니다.", exception.getMessage());
    }

    @Test
    @DisplayName("Post 삭제 - 삭제된 게시물")
    void testDeletePostDeletePost() {
        //given
        given(postRepository.findById(any(Long.class))).willReturn(
            Optional.ofNullable(TEST_DELETE_POST));


        //when
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
            postService.deletePost(TEST_USER, TEST_DELETE_POST_ID));

        //then
        assertEquals("삭제된 게시물 입니다.", exception.getMessage());
    }

    @Test
    @DisplayName("Post 삭제 - 타인 게시물")
    void testDeletePostNoPermission() {
        //given
        given(postRepository.findById(any(Long.class))).willReturn(Optional.ofNullable(TEST_POST));


        //when
        PermissionDeniedException exception = assertThrows(PermissionDeniedException.class, () ->
            postService.deletePost(TEST_ANTHER_USER, TEST_POST_ID));

        //then
        assertEquals(PERMISSION_DENIED, exception.getErrorCode());
    }
}
